import java.util.Arrays;
import java.util.Scanner;

public class DESEncrypt {

  public static void main(String[] args){
    Scanner scan = new Scanner(System.in);
    System.out.println("Enter a twelve bit binary string"); 
    String input = scan.nextLine();
    int[] bitString = new int[input.length()];
    for (int i=0; i<bitString.length; i++){
      bitString[i] = input.charAt(i) - '0';
    }  
    System.out.println("Enter a nine bit key"); 
    String in = scan.nextLine();
    int[] key = new int[in.length()];
    for (int i = 0; i < in.length(); i++){ 
      key[i] = in.charAt(i) - '0';  
    }
    
  //encrypts the user's message using the key provided by the user
    fourRoundEncryptPrint(bitString,key); 


    System.out.println("Testing for weak keys...");
    System.out.println("");
    
    
    
    //tests for weak keys in the regular DES algorithm
    boolean allStrong = false;
    int weakKeys = 0;
      for(int i = 0; i < Math.pow(2,9); i++){
        for(int j = 0; j < Math.pow(2,12); j++){
          allStrong= false;
          int[] firstRound = fourRoundEncrypt(testString(j), testK(i));
          int[] secondRound = fourRoundEncrypt(firstRound, testK(i));

          if(!Arrays.equals(testString(j), secondRound)){
            allStrong = true;
          }
          
         
        }
        if( allStrong == false){
          printArray(testK(i));
          System.out.println(" is weak.");
          weakKeys++; 
        }  
      }
          
      System.out.println("There are " + weakKeys + " weak keys");
      System.out.println("");
      
      //tests for weak keys in the modified DES algorithm
      System.out.println("Testing for weak keys in the modified algorithm...");
      System.out.println("");
      allStrong = false;
      weakKeys = 0;
        for(int i = 0; i < Math.pow(2,9); i++){
          allStrong = false;
          for(int j = 0; j < Math.pow(2,12); j++){
            int[] firstRound = fourRoundEncryptModified(testString(j), testK(i));
            int[] secondRound = fourRoundEncryptModified(firstRound, testK(i));

            if(!Arrays.equals(testString(j), secondRound)){
              allStrong = true;
    
            }
            
           
          }
          if( allStrong == false){
            System.out.print("The key ");
            printArray(testK(i));
            System.out.println(" is weak.");
            weakKeys++; 
          }  
        }
        System.out.println("");    
        System.out.println("There are " + weakKeys + " weak keys.");


  }
/**
 * This is the expander function for the Feistal System.
 * @param bitString the right half of the 12 bit bitstring that is being encrypted
 * @return the right half expanded to 8 bits
 */
  public static int[] E(int[] bitString){
    int[] EbitString = new int[8];
    EbitString[0] = bitString[0];
    EbitString[1] = bitString[1];
    EbitString[2] = bitString[3];
    EbitString[3] = bitString[2];
    EbitString[4] = bitString[3];
    EbitString[5] = bitString[2];
    EbitString[6] = bitString[4];
    EbitString[7] = bitString[5];
    return EbitString;
  }
/**
 * XORs 2 arrays.
 * @param array1 one of the arrays to be XORed
 * @param array2 one of the arrays to be XORed
 * @return the 2 arrays XORed together
 */
  public static int[] XOR(int[] array1, int[] array2){
    int[] xor = new int[array1.length];
    for(int i=0; i<array1.length; i++){
      xor[i] = (array1[i] + array2[i])%2;
    }

    return xor;
  }
/**
 * Performs the function that is part of the Feistal System
 * @param right the right half of the 12 bit bitstring
 * @param llave the 9 bit encryption key
 * @return the 6 bit result of the function 
 */
  public static int[] function (int[] right, int[] llave){
    String[][] s1 = {
        {"101", "010", "001", "110", "011", "100", "111", "000"},
        {"001", "100", "110", "010", "000", "111", "101", "011"}
    };

    String[][] s2 = {
        {"100", "000", "110", "101", "111", "001", "011", "010"},
        {"101", "011", "000", "111", "110", "010", "001", "100"}
    };
    int[] eRight = E(right);
    int[] kXORR = XOR(eRight, llave);
    int[] left4 = new int[4];
    int[] right4 = new int[4];
    for(int i = 0; i<left4.length; i++){
      left4[i] = kXORR[i];
    }
    for(int i = 0; i<right4.length; i++){
      right4[i] = kXORR[i+4];
    }
    String leftF = s1[left4[0]][left4[1]*4+left4[2]*2+left4[3]*1];
    String rightF = s2[right4[0]][right4[1]*4+right4[2]*2+right4[3]*1];
    int[] fRK = new int[6];
    for(int i = 0; i<3; i++){
      fRK[i] = leftF.charAt(i)-'0';
    }
    for(int i = 3; i<6; i++){
      fRK[i] = rightF.charAt(i-3)-'0';
    }
    return fRK;
  }
  
  /**
   * Creates a certain number key of 8 bits from a 9 bit key.
   * @param key the 9 bit key for the whole encryption process
   * @param keynum the key number
   * @return
   */
  public static int[] keyI (int[] key, int keynum){
    int[] keyi = new int[8];
    for(int i=0; i<8; i++){
      keyi[i] = key[(i+(keynum-1))%9];
    }
    return keyi;
  }
/**
 * Prints an array.
 * @param array the array to be printed
 */
  public static void printArray(int[] array){
    for(int i = 0; i<array.length; i++){
      System.out.print(array[i]);
    }
  }
/**
 * Takes an integer then converts it to a binary string and stores it as an integer array.
 * @param key the integer to be converted into a binary string
 * @return the integer as a 9 bit binary string stored in an integer array
 */
  public static int[] testK(int key){    
    String keyS = Integer.toString(key,2);
    int[] keyB = new int[9];
    for (int i = 0; i < 9-keyS.length(); i++){ 
      keyB[i] = 0;  
    }
    for(int i = 9-keyS.length(); i<9; i++){
      keyB[i] = keyS.charAt(i-(9-keyS.length())) - '0';
    }
    return keyB;
  }
/**
 * Takes an integer then converts it to a binary string and stores it as an integer array.
 * @param string the integer to be converted into a binary string
 * @return the integer as a 12 bit binary string stored in an integer array
 */
  public static int[] testString(int string){
    String stringS = Integer.toString(string, 2);
    int[] stringB = new int[12];
    for (int i = 0; i < 12-stringS.length(); i++){ 
      stringB[i] = 0;  
    }
    for(int i = 12-stringS.length(); i < 12; i++){
      stringB[i] = stringS.charAt(i-(12-stringS.length())) - '0';
    }
    return stringB;
  }
/**
 * Performs one round of the Feistal System.
 * @param string the bitstring to be encrypted
 * @param key the key that is being used to encrypt
 * @param knum the number of the key being used (which bit of the 9 bit key to start at)
 * @return the bitstring encrypted one time
 */
  public static int[] encrypt(int[] string, int[] key, int knum){
    int[] bitStringR = new int[string.length/2];
    int[] bitStringL = new int[string.length/2];
    for (int i = 0; i < string.length/2; i++){ //left half
      bitStringL[i] = string[i];  
    }

    for (int i = string.length/2; i < string.length; i++){ //right half
      bitStringR[i-string.length/2] = string[i];  
    }
    int[] Lnext = bitStringR;
    int[] Rnext = XOR(bitStringL, function(bitStringR, keyI(key, knum)));
    int[] nextRound = new int[12];
    for(int i = 0; i<6; i++){
      nextRound[i] = Lnext[i];
    }
    for(int i = 6; i<12; i++){
      nextRound[i] = Rnext[i-6];
    }
    return nextRound;
  }
/**
 * This performs 4 rounds of the regular DES algorithm.
 * @param bitString the bitstring to be encrypted
 * @param key the key used to encrypt the bitstring
 * @return the encrypted bitstring
 */
  public static int[] fourRoundEncrypt(int[] bitString, int[] key){
    int num = 1;
    while(num<5){
      bitString = encrypt(bitString, key, num);
      num++;

    }

    return bitString;
  }
/**
 * Prints the bitstring that has been encrypted by the regular DES algorithm.
 * @param bitString the bitstring to be encrypted
 * @param key the key used to encrypt the message
 */
  public static void fourRoundEncryptPrint(int[] bitString, int[] key){
    int num = 1;
    while(num<5){
      System.out.print("round " + num + " ");
      printArray(encrypt(bitString, key, num));
      System.out.println("");
      bitString = encrypt(bitString, key, num);
      num++;

    }


  }
/**
 * This is the modified version of the DES algorithm that swaps the left and right halves 
 * after for rounds of the Feistel system.
 * @param bitString the bitstring to be encrypted
 * @param key the key used to encrypt the message 
 * @return the encrypted bitstring
 */
  public static int[] fourRoundEncryptModified(int[] bitString, int[] key){ 
    int num = 1;
    while(num<5){
      bitString = encrypt(bitString, key, num);
      num++;

    }
    int[] bitStringNew = new int[12];

    for(int i = 0; i<6; i++){
      bitStringNew[i] = bitString[i+6];
    }
    for(int i = 6; i<12; i++){
      bitStringNew[i] = bitString[i-6];
    }
    return bitStringNew;
  }

}