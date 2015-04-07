OldSpiceVoicemail.java
Jason Hsu

The program is coded in java. 
To run in cmd, you'll need the jdk package available on the java website.
Use javac OldSpiceVoicemail.java to compile once in the proper directory.
Use java OldSpiceVoicemail to run the program with menu displays.
Use java OldSpiceVoicemail -g m -n 123.123.1234 -r 1 -e 23 -o voicemail.mp3 with your specified choices to run it directly.

Male and Female Reasons and Endings were put into 4 separate arrays for better display and easier coordination.
The mp3 files are organized in the same way correspondingly. 
Only the unique names of the mp3 files are stored in the arrays as the extended url can be gotten rid of with a preset line.

If arg.length is 0, then the program determines that it wants to be run through with display menu choices.
If arg.length is exactly 10 with the correct order of flags then it would run through the choices specified and produce the final mp3 directly.
No other arg.length would run.

Depends on the gender chosen, the preset greetings would auto-load while leaving the rest of the choices up to the user.

The user's choices would be stored in a list and compiled/merged once confirmed. 

Note that the naming of the final mp3 has to end up .mp3 otherwise the final product would be of unknown file type.

See comments for code specific information.