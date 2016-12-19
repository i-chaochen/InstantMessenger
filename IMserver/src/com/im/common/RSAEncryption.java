/**
 * this is for RSA encryption and decryption
 */
package com.im.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPublicKey;
import java.util.LinkedList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


public class RSAEncryption {
	/**
	 * static object for en/decryption method
	 */
	private static RSAEncryption instance = new RSAEncryption();
	
	  //the key pair
	private KeyPair keyPair = null;
	
	//the static encryption method
	public static RSAEncryption getInstance()
	{
		return instance;
	}
	
	//initialize container
	public RSAEncryption()
	{
		this.initialize();
	}
	
	/**
	 * get this key pair from RSAKey.911
	 */
	private void initialize()
	{
		InputStream is = getClass().getClassLoader().getResourceAsStream("RSAKey.911");
		try {
			ObjectInputStream ois = new ObjectInputStream(is);
			keyPair = (KeyPair) ois.readObject();
			
			ois.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * get the public key to encrypt
	 * @return
	 */
	private RSAPublicKey getPuKey()
	{
		return (RSAPublicKey) keyPair.getPublic();
	}
	
	/**
	 * get the private key to decrypt
	 * @return
	 */
	private RSAPrivateCrtKey getPrKey()
	{
		return (RSAPrivateCrtKey) keyPair.getPrivate();
	}
	
	/**
	 * encrypt by public key, the length is 100
	 * @param plainText
	 * @return
	 * @throws NoSuchPaddingException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	 */
	public byte[] encrypt(String plainText) 
	{
		try{
			//the list for get the plain text("UTF8")
			List<byte[]> plainBytesList =  getChunks(plainText.getBytes("UTF8"), 100);
			//the total encrypted byte[]
			byte[] enc = new byte[plainBytesList.size() * 128];
	
			//get the encrypt cipher
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, this.getPuKey());
			
			//encrypt
			byte[] cipherText = null;
			int destPos = 0;
			//copy the plain text into cipher
			for(int i = 0; i < plainBytesList.size(); i++)
			{	//add plain chunk into cipher by index
				cipherText = cipher.doFinal(plainBytesList.get(i));
				System.arraycopy(cipherText, 0, enc, destPos, cipherText.length);
				destPos += cipherText.length; 
			}//return the encrypted chunks list
			return enc;
			
		}catch(NoSuchAlgorithmException e){
			e.printStackTrace();
		}catch(NoSuchPaddingException e){
			e.printStackTrace();
		}catch(InvalidKeyException e){
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * decrypt by private key, the length is 128
	 * @param cipherText
	 * @return
	 */
	public String decrypt(byte[] cipherText)
	{
		String retStr = null;
		try{
			//the list for cipher list
			List<byte[]> cipherBytesList = getChunks(cipherText, 128);
			//the list for adding plain text
			List<byte[]> planBytesList = new LinkedList<byte[]>();
			
			//get the decryptd cipher
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.DECRYPT_MODE, this.getPrKey());
			
			//decrypt by cipher bytes[] list 
			int size = 0;
			int destPos = 0;
			byte[] plainText = null;
			for(int i = 0; i < cipherBytesList.size(); i++)
			{
				plainText = cipher.doFinal(cipherBytesList.get(i));
				size += plainText.length;
				planBytesList.add(plainText);
			}
			
			//add these decrypted byte[] chunks to plain text
			destPos = 0;
			byte[] planBytes = new byte[size];
			for(int i = 0; i < planBytesList.size(); i++)
			{
				plainText = planBytesList.get(i);//String plain text
				System.arraycopy(plainText, 0, planBytes, destPos, plainText.length);
				destPos += plainText.length;
			}
			//the UTF8 String
			retStr = new String(planBytes, "UTF8");
			
		}catch(NoSuchAlgorithmException e){
			e.printStackTrace();
		}catch(NoSuchPaddingException e){
			e.printStackTrace();
		}catch(InvalidKeyException e){
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return retStr;
	}
	
	
	 /**
	  * an array which is split by ¡°size¡± ones. 
	  * Every one has ¡°length¡± elements. 
	  * when this byte[] chunk cannot be divided as integer
	  * the last array wouldn¡¯t have ¡°length¡± ones elements.
	  */

	private List<byte[]> getChunks(byte[] bs, int length) throws UnsupportedEncodingException
    {
            List<byte[]> bytesList = new LinkedList<byte[]>();
           //the length of byte[]
            int len = bs.length;
            //the size of array
            int size = (int)Math.ceil(((double)len)/length);   
           
            int total = 0;
            //copied position
            int copiedPos = 0;
            //byte[] buffer to be added
            byte[] buf = null;
            //copy these bytes into buffer one by one
            for(int i = 0; i < size; i++)   
            {		//where are we copying 
                    copiedPos = length * i;
                    // the copied ones
                    total = len - copiedPos;
                    // when the total is more than chunk's length, the total is chunk's length
                    //otherwise, total is total
                    total = total > length ?  length : total;
                    buf = new byte[total];
                    //copy these bytes into buffer, add to the list
                    System.arraycopy(bs, copiedPos, buf, 0, total);
                    bytesList.add(buf);
            }
            return bytesList;
    }
}









