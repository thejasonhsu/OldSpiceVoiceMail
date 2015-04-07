import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.*;

public class OldSpiceVoicemail 
{	
	private static String GENDER[] = {"Male", "Female"};
	private static int NUM_REASONS[] = {4, 5};
	private static int NUM_ENDINGS[] = {5, 2};
	
	//set reasons and endings into arrays
	private static String MALE_REASONS[] = {"1. Building an orphanage with their barehands while playing a sweet, sweet lulluby for those children with two mallets against their abs xylophone. ", "2. Cracking walnuts with their man mind. ", "3. Polishing their monocle smile. ", "4. Ripping out mass loads of weights. "};
	private static String FEMALE_REASONS[] = {"1. Ingesting my delicious Old Spice man smell. ", "2. Listening to me read romantic poetry while I make a bouquet of paper flowers from each read page. ", "3. Enjoying a lobster dinner I prepared just for her while carrying her on my back safely through piranha infested waters. ", "4. Being serenaded on the moon with the view of the earth while surviving off the oxygen of lungs via a passionate kiss. ", "5. Riding a horse backwards with me. "};
	private static String MALE_ENDINGS[] = {"1. I'm on a horse. ", "2. Jingle. ", "3. I'm on a phone. ", "4. Swan Dive. ", "5. This voicemail is now diamonds. "}; 
	private static String FEMALE_ENDINGS[] = {"1. She will get back to you. ", "2. Thanks for calling. "};
	
	//set mp3 files corresponding to the above arrays
	private static String MALE_REASON_FILE[] = {"m-r1-building", "m-r2-cracking_walnuts", "m-r3-polishing_monocole", "m-r4-ripping_weights"};
	private static String FEMALE_REASON_FILE[] = {"f-r1-ingesting_old_spice", "f-r2-listening_to_reading", "f-r3-lobster_dinner", "f-r4-moon_kiss", "f-r5-riding_a_horse"};
	private static String MALE_ENDING_FILE[] = {"m-e1-horse", "m-e2-jingle", "m-e3-on_phone", "m-e4-swan_dive", "m-e5-voicemail"};
	private static String FEMALE_ENDING_FILE[] = {"f-e1-she_will_get_back_to_you", "f-e2-thanks_for_calling"};
  
	public static void main(String args[]) throws FileNotFoundException, MalformedURLException, IOException
	{
		Scanner input = new Scanner(System.in);
		String response, phoneNo, reasons, endings;
		int gender;
		
		//if args.length is 0 then the program runs with display menu
		if(args.length == 0)
		{
			do
			{	
				//call gender and phone number functions
				System.out.println();
				gender = getGender(input);
				System.out.println();
				phoneNo = getPhoneNumber(input);
				System.out.println();
				
				//if male is chosen
				if(gender == 1)
				{
					reasons = getMaleReason(input);
					System.out.println();
					endings = getMaleEnding(input);
					System.out.println();
				}
				//if female is chosen
				else
				{
					reasons = getFemaleReason(input);
					System.out.println();
					endings = getFemaleEnding(input);
					System.out.println();
				}
				
				System.out.println("Gender : " + GENDER[gender - 1]);
				System.out.println("PhoneNo: " + phoneNo);
				System.out.print("Reasons: ");
				
				//iterate through the arrays to display choices depending on gender selected
				for(int i = 0; i < reasons.length(); i++)
					System.out.print((gender == 1) ? MALE_REASONS[reasons.charAt(i) - '1'] : FEMALE_REASONS[reasons.charAt(i) - '1'] + " ");
				System.out.println();
		
				System.out.print("Endings: ");
				
				//same thing as reasons, now for endings
				for(int i = 0; i < endings.length(); i++)
					System.out.print((gender == 1) ? MALE_ENDINGS[endings.charAt(i) - '1'] : FEMALE_ENDINGS[endings.charAt(i) - '1'] + " ");
				System.out.println();
				
				//confirmation prompt
				System.out.print("Do you want to confirm (y/n) > ");
				response = input.next().toLowerCase();
				
				//error checking
				while(!response.equals("y") && !response.equals("n"))
				{
					System.out.print("Invalid Input, Do you want to confirm (y/n) > ");
					//re-confirm
					response = input.next().toLowerCase();
				}
			} while(response.charAt(0) == 'n'); //whole thing restarts if confirmation is n

			//file naming after confirmation is y
			System.out.print("What do you want to call this file > ");
			String filename = input.next();
			
			//download the mp3 files according to the user-selected lists
			ArrayList<URL> files = getURLFiles(gender, phoneNo, reasons, endings);
			
			InputStream is[] = new InputStream[files.size()];
			for(int i = 0; i < files.size(); i++)
				is[i] = files.get(i).openConnection().getInputStream();
			
			//fileGroup contains the list of mp3 files to be merged
			List<File> fileGroup = importStreams(is, files.size());
			printToFile(filename, gender, phoneNo, reasons, endings);
			mergeFiles(fileGroup, new File(filename));
			
			//new File becomes the final merged mp3
			//iterates through fileGroup to delete downloaded segments used to produce the final mp3
			for(int i = 0; i < fileGroup.size(); i++)
			{
				File file = new File("file" + i + ".mp3");
				file.createNewFile();
				file.delete();
			}
		}
		//if args.length is 10, then the programs run with choices made at command prompt
		else if(args.length == 10)
		{
			//runs only if arguments are in this order -g -n -r -e -o with specified choices after each flag
			if(args[0].equals("-g") && args[2].equals("-n") && args[4].equals("-r") && args[6].equals("-e") && args[8].equals("-o"))
			{
				//setting char to number
				if(checkGender(args[1]))
				{
					gender = (args[1].equals("m") ? 1 : 2);
					//error checking before proceeding
					if(isValidPhoneNo(args[3]))
					{
						phoneNo = args[3];
						if(isValidChoices(args[5], NUM_REASONS[gender - 1]))
						{
							reasons = args[5];
							if(isValidChoices(args[7], NUM_ENDINGS[gender - 1]))
							{
								endings = args[7];
								String filename = args[9];
								
								//same file grabbing method as above, now just running through the command line by user choice
								ArrayList<URL> files = getURLFiles(gender, phoneNo, reasons, endings);
									
								InputStream is[] = new InputStream[files.size()];
								for(int i = 0; i < files.size(); i++)
									is[i] = files.get(i).openConnection().getInputStream();
									
								List<File> fileGroup = importStreams(is, files.size());
								printToFile(filename, gender, phoneNo, reasons, endings);
								mergeFiles(fileGroup, new File(filename));
									
								for(int i = 0; i < fileGroup.size(); i++)
								{
									File file = new File("file" + i + ".mp3");
									file.createNewFile();
									file.delete();
								}
							}
							//error checking brackets
							else
								System.out.println("Error: Ending(s) is/are invalid");
						}
						else
							System.out.println("Error: Reason(s) is/are invalid");
					}
					else
						System.out.println("Error: Phone number is invalid.");
				}
				else
					System.out.println("Error: Gender is invalid.");
			}
			else
				System.out.println("Error: Flags not correct (must be in -g, -n, -r, -e, -o)");
		}
		input.close();
	}
	
	public static boolean checkGender(String choice)
	{
		return (choice.equals("m") || choice.equals("f"));
	}
	
	//create the list of mp3 files needed to be used for the final product
    public static List<File> importStreams(InputStream is[], int num)
    {
        LinkedList<File> list = new LinkedList<File>();
    	FileOutputStream outputStream;
        for(int i = 0; i < num; i++)
        {
            try
            {
            	outputStream = new FileOutputStream(new File("file" + i + ".mp3"));

                int read = 0;
		        byte[] bytes = new byte[1024];
 
	            while ((read = is[i].read(bytes)) != -1) 
                    outputStream.write(bytes, 0, read);
            	outputStream.close();
		    }
		    catch (IOException e)
		    {
		        
		    }
		    list.add(new File("file" + i + ".mp3"));
        }
        return list;
    }
    	
	public static ArrayList<URL> getURLFiles(int gender, String phoneNo, String reasons, String endings) throws MalformedURLException
	{
		//preset all mp3 source with the same beginning
		final String beginning = "http://www-bcf.usc.edu/~chiso/itp125/project_version_1/";
		ArrayList<URL> list = new ArrayList<URL>();
		
		//grab these preset non-choosable mp3 files after gender has been selected
		if(gender == 1)
		{
			list.add(new URL(beginning + "m-b1-hello.mp3"));
			list.add(new URL(beginning + "m-b2-have_dialed.mp3"));
		}
		else
		{
			list.add(new URL(beginning + "f-b1-hello_caller.mp3"));
			list.add(new URL(beginning + "f-b2-lady_at.mp3"));
		}
		
		for(int i = 0; i < phoneNo.length(); i++)
			if(phoneNo.charAt(i) >= '0' && phoneNo.charAt(i) <= '9')
				list.add(new URL(beginning + phoneNo.charAt(i) + ".mp3"));
		
		if(gender == 1)
			list.add(new URL(beginning + "m-r0-cannot_come_to_phone.mp3"));
		else
		{
			list.add(new URL(beginning + "f-r0.1-unable_to_take_call.mp3"));
			list.add(new URL(beginning + "f-r0.2-she_is_busy.mp3"));
		}
		
		for(int i = 0; i < reasons.length(); i++)
		{
			String ans = (gender == 1) ? MALE_REASON_FILE[reasons.charAt(i) - '1'] : FEMALE_REASON_FILE[reasons.charAt(i) - '1'];
			list.add(new URL(beginning + ans + ".mp3"));
		}
		
		for(int i = 0; i < endings.length(); i++)
		{
			String ans = (gender == 1) ? MALE_ENDING_FILE[endings.charAt(i) - '1'] : FEMALE_ENDING_FILE[endings.charAt(i) - '1'];
			list.add(new URL(beginning + ans + ".mp3"));
		}
		
		if(gender == 1)
		{
			list.add(new URL(beginning + "m-leave_a_message.mp3"));
			list.add(new URL(beginning + "m-youre_welcome.mp3"));
		}
		
		return list;
	}
	
	//mergeFiles function that goes through the list of mp3 files and connect them one after another into one file
    public static void mergeFiles(List<File> files, File into) throws IOException 
    {
        try (BufferedOutputStream mergingStream = new BufferedOutputStream(new FileOutputStream(into))) 
        {
            for (File f : files) 
            {
                Files.copy(f.toPath(), mergingStream);
            }
        }
    }
    
    //output the list of mp3 files used to create the final product into a .txt file
	public static void printToFile(String filename, int gender, String phoneNo, String reason, String endings) throws FileNotFoundException
	{
		PrintWriter writer = new PrintWriter(new File(filename + ".txt"));
		
		//the presets
		if(gender == 1)
		{
			writer.println("m-b1-hello.mp3");
			writer.println("m-b2-have_dialed.mp3");
		}
		else
		{
			writer.println("f-b1-hello_caller.mp3");
			writer.println("f-b2-lady_at.mp3");
		}
		
		writer.println((gender == 1) ? "male" : "female");
		for(int i = 0; i < phoneNo.length(); i++)
			if(phoneNo.charAt(i) >= '0' && phoneNo.charAt(i) <= '9')
				writer.println(phoneNo.charAt(i) + ".mp3");
		
		//same functions based on choice
		for(int i = 0; i < reason.length(); i++)
		{
			String ans = (gender == 1) ? MALE_REASON_FILE[reason.charAt(i) - '1'] : FEMALE_REASON_FILE[reason.charAt(i) - '1'];
			writer.println(ans + ".mp3");
		}
		
		for(int i = 0; i < endings.length(); i++)
		{
			String ans = (gender == 1) ? MALE_ENDING_FILE[endings.charAt(i) - '1'] : FEMALE_ENDING_FILE[endings.charAt(i) - '1'];
			writer.println(ans + ".mp3");
		}
		writer.close();
	}
	
	//gender menu
	public static int getGender(Scanner input)
	{
		System.out.println("The Old Spice Version: ");
		System.out.println(" 1. Male");
		System.out.println(" 2. Female");
		int choice = -1;
		
		do
		{
			System.out.print("Which version would you like > ");
			//error checking
			try
			{
				choice = input.nextInt();
				if(choice != 1 && choice != 2)
					System.out.println("Invalid Input");
			}
			catch(InputMismatchException e)
			{
				System.out.println("Invalid Input");
				input.nextLine();
			}
		} while (choice != 1 && choice != 2);
		
		return choice;
	}
	
	//phone number menu
	public static String getPhoneNumber(Scanner input)
	{
		System.out.print("Enter Your Phone Number > ");
		String phoneNo = input.next();
		
		//error checking; function in next block
		while(!isValidPhoneNo(phoneNo))
		{
			System.out.print("Invalid Input, Enter Your Phone Number > ");
			phoneNo = input.next();
		}
		
		return phoneNo;
	}
	
	//valid phone number checking
	private static boolean isValidPhoneNo(String phoneNo)
	{
		//allow brackets and special characters
		if(phoneNo.startsWith("("))
		{
			for(int i = 0; i < phoneNo.length(); i++)
			{
				if(i == 0)
				{
					if(phoneNo.charAt(0) != '(')
						return false;
				}
				else if(i == 4)
				{
					if(phoneNo.charAt(4) != ')')
						return false;
				}
				else if(i == 8)
				{
					if(phoneNo.charAt(8) != '-')
						return false;
				}
				else if(phoneNo.charAt(i) < '0' || phoneNo.charAt(i) > '9')
					return false;
			}
			return true;
		}
		//phone number entered without special chars
		else if(phoneNo.length() == 10)
		{
			for(int i = 0; i < phoneNo.length(); i++)
				if(phoneNo.charAt(i) < '0' || phoneNo.charAt(i) > '9')
					return false;
			return true;
		}
		//phone number entered with - and . hence +2 in length
		else if(phoneNo.length() == 12)
		{
			for(int i = 0; i < phoneNo.length(); i++)
			{
				if(i == 3)
				{
					if(phoneNo.charAt(3) != '-' && phoneNo.charAt(3) != '.')
						return false;
				}
				else if(i == 7)
				{
					if(phoneNo.charAt(3) != phoneNo.charAt(7))
						return false;
				}
				else if(phoneNo.charAt(i) < '0' || phoneNo.charAt(i) > '9')
					return false;
			}
			return true;
		}
		else
			return false;
	}
  		
	//male reason menu
	public static String getMaleReason(Scanner input)
	{
		for(int i = 0; i < MALE_REASONS.length; i++)
			System.out.println(MALE_REASONS[i]);
        System.out.print("Enter Your Reason(s) > ");
        
		String reasons = input.next();
		
		//error checking; function in later block
		while(!isValidChoices(reasons, MALE_REASONS.length))
		{
			System.out.print("Invalid Input, Enter Your Reason(s) > ");
			reasons = input.next();
		}
		
		return reasons;
	}
	
	//female reason menu
    public static String getFemaleReason(Scanner input)
    {
    	for(int i = 0; i < FEMALE_REASONS.length; i++)
    		System.out.println(FEMALE_REASONS[i]);
        System.out.print("Enter Your Reason(s) > ");
        
		String reasons = input.next();
		
		//error checking; function in later block
		while(!isValidChoices(reasons, FEMALE_REASONS.length))
		{
			System.out.print("Invalid Input, Enter Your Reason(s) > ");
			reasons = input.next();
		}
		
		return reasons;
    }
	
    //user input cannot be less than 1 or greater than the max number of choices
	public static boolean isValidChoices(String choices, int max)
	{
		for(int i = 0; i < choices.length(); i++)
			if(choices.charAt(i) - '1' < 0 || choices.charAt(i) - '1' >= max)
				return false;
		return true;
	}
	
	//male ending menu
    public static String getMaleEnding(Scanner input)
    {
    	for(int i = 0; i < MALE_ENDINGS.length; i++)
    		System.out.println(MALE_ENDINGS[i]);
        System.out.print("Enter Your Ending(s) > ");
        
		String endings = input.next();
		
		//error checking
		while(!isValidChoices(endings, MALE_ENDINGS.length))
		{
			System.out.print("Invalid Input, Enter Your Choice(s) > ");
			endings = input.next();
		}
		
		return endings;
    }
    
    //female ending menu
	public static String getFemaleEnding(Scanner input)
    {
    	for(int i = 0; i < FEMALE_ENDINGS.length; i++)
    		System.out.println(FEMALE_ENDINGS[i]);
        System.out.print("Enter Your Ending(s) > ");
        
		String endings = input.next();
		
		//error checking
		while(!isValidChoices(endings, FEMALE_ENDINGS.length))
		{
			System.out.print("Invalid Input, Enter Your Choice(s) > ");
			endings = input.next();
		}
		
		return endings;
    }
}