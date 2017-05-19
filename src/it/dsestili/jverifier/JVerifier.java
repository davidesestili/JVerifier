package it.dsestili.jverifier;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import it.dsestili.jhashcode.core.Core;

public class JVerifier 
{

	public static void main(String[] args) 
	{
		if(args.length > 0)
		{
			new JVerifier().verify(args[0], args[1]);
		}
		else
		{
			System.out.println("Usage: param 1: file name, param 2: algorithm");
		}
	}
	
	private void verify(String param1, String param2)
	{
		File file = new File(param1);
		
		if(!file.exists())
		{
			System.out.println("File does not exist");
		}
		
		if(!file.isFile())
		{
			System.out.println("Is not a file");
		}
		
		try 
		{
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader reader = new BufferedReader(isr);
			
			int okCount = 0, doesNotMatchCount = 0;
			
			String lineOfText;
			while((lineOfText = reader.readLine()) != null)
			{
				/* Versione 1 
				lineOfText = lineOfText.replace("*", " ");
				String[] lineSplit = lineOfText.split("  ");
				-------------------- */
				
				StringBuilder hashStringBuilder = new StringBuilder();
				StringBuilder fileNameStringBuilder = new StringBuilder();
				int i;
				for(i = 0; i < lineOfText.length(); i++)
				{
					char c = lineOfText.charAt(i);
					
					if(c == ' ')
					{
						i += 2;
						break;
					}
					
					hashStringBuilder.append(c);
				}

				String hash = hashStringBuilder.toString();
				
				while(i < lineOfText.length())
				{
					char c = lineOfText.charAt(i);
					fileNameStringBuilder.append(c);
					i++;
				}

				String fileName = fileNameStringBuilder.toString();
				
				File currentFile = new File(fileName);
				
				Core core = new Core(currentFile, param2);
				String generatedHash = core.generateHash();
				
				System.out.print(currentFile.getName() + " ");
				
				if(generatedHash.equalsIgnoreCase(hash))
				{
					System.out.println("OK");
					okCount++;
				}
				else
				{
					System.out.println("Does not match");
					doesNotMatchCount++;
				}
			}
			
			reader.close();
			System.out.println("OK: " + okCount + " - Does not match: " + doesNotMatchCount);
		} 
		catch(Throwable e)
		{
			e.printStackTrace();
		}
		
	}
	
}
