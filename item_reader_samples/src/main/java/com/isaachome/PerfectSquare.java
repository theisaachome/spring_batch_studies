package com.isaachome;

public class PerfectSquare {
    public static void main(String[] args) {
        System.out.println("Next perfect square to 7 is: "+ f(6));
        System.out.println("Next perfect square to 7 is: "+ f(36));
        System.out.println("Next perfect square to 7 is: "+ f(0));
        System.out.println("Next perfect square to 7 is: "+ f(-5));
    }

    public static int f(int n) {
        if(n < 0) return 0;
        int sqrt = (int) Math.sqrt(n);
         int nextInt = sqrt + 1;
        int nextPerfectSquare = nextInt * nextInt;
//        int i, ans;
//
//        for(i = 1; ; i++) {
//            ans = i * i;
//            if(n < ans) break;
//        }
        return nextPerfectSquare;
    }
}
