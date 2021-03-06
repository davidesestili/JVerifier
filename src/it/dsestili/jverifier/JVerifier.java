/*
JVerifier an hash code verifier
Copyright (C) 2017 Davide Sestili

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package it.dsestili.jverifier;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import it.dsestili.jhashcode.core.Core;

public class JVerifier 
{

	public static void main(String[] args) 
	{
		if(args.length == 2)
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
			return;
		}
		
		if(!file.isFile())
		{
			System.out.println("Is not a file");
			return;
		}
		
		try 
		{
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader reader = new BufferedReader(isr);
			
			int okCount = 0, doesNotMatchCount = 0, notFoundCount = 0;

			File currentFile = null;
			
			String lineOfText;
			while((lineOfText = reader.readLine()) != null)
			{
				try
				{
					String[] hashAndFileName = getHashAndFileName(lineOfText);
					
					String hash = hashAndFileName[0];
					String fileName = hashAndFileName[1];
					
					currentFile = new File(fileName);
					
					Core core = new Core(currentFile, param2);
					String generatedHash = core.generateHash();
					
					System.out.print(currentFile.getAbsolutePath() + " ");
					
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
				catch(FileNotFoundException e)
				{
					System.out.println(currentFile.getAbsolutePath() + " not found");
					notFoundCount++;
				}
			}
			
			reader.close();
			System.out.println("OK: " + okCount + " - Does not match: " + doesNotMatchCount + " - Not found: " + notFoundCount);
		} 
		catch(Throwable e)
		{
			e.printStackTrace();
		}
	}

	public static String[] getHashAndFileName(String lineOfText)
	{
		String[] hashAndFileName = new String[2];
		
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

		hashAndFileName[0] = hashStringBuilder.toString();
		
		while(i < lineOfText.length())
		{
			char c = lineOfText.charAt(i);
			fileNameStringBuilder.append(c);
			i++;
		}

		hashAndFileName[1] = fileNameStringBuilder.toString();
		
		return hashAndFileName;
	}
	
}
