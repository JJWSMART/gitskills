import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

public class CalendarCanada{
	
	//(7, 5)  ----7
	public static String padded(int n, int width){
		String s = Integer.toString(n);
		for(int i = s.length(); i < width; i++){
			s = " " + s;
		}
		s = s + " |";
		return s;
	}

	public static String printfLine(int space, int numDays){
		
		String s = "";
		int idx = 1;
		for(int i = 0; i < 7; i++){
			
			if(idx>numDays) break;
			s = s + "|";
			
			for(int j = 0; j < 7; j++){
				
				if(space > 0){
					s = s + "     |";
					space--;
					continue;
				}
				
				if(idx > numDays){
					s = s + "     |";
				}else{
					s = s + padded(idx, 4);
					idx++;
				}
			}
			
			if(idx <= numDays){
				s = s + "\n";
			}
		}

		return s;
	}

	public static void printfUserInput(){
		// user input
		System.out.printf("Number of days in month ?");
		Scanner sc = new Scanner(System.in);
		int m = sc.nextInt();
		
		System.out.printf("Data of first sunday ?");
		int f = sc.nextInt();

		if(f < 1 || f > 7){
			System.out.println("Wrong startingSunday: reenter again !");
			System.exit(0);
		}

		printfdays();

		printfSeparator();

		outputCalendar(m, f);

		printfSeparator();
	}

	public static void printfdays(){
		System.out.println("   Su   Mo    Tu    We    Th    Fr    Sa   ");
	}
	
	public static void printfSeparator(){
		System.out.println("+-----+-----+-----+-----+-----+-----+-----+");
	}

	public static void outputCalendar(int numDays, int startingSunday){

		int space = 0;
		if(startingSunday != 1){
			space = 8 - startingSunday;
		}

		System.out.println(printfLine(space, numDays));
		
	}

	public static void main(String[]args){


		printfUserInput();
		

	}
}