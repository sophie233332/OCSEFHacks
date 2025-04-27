package com.yourname.plantgame.helperclass;

import java.util.Random;

public class Random2DArray<T>{
    T[][] array2D;
    int totalLength=0;
    public Random2DArray(T[][] array2D){
        this.array2D=array2D.clone();
        for(int r=0;r<array2D.length;r++){
            this.array2D[r]=array2D[r].clone();
            totalLength+=array2D[r].length;
        }
    }
    private static int randomInt(int a,int b){//[a,b)
        return (int) ((b-a)*Math.random())+a;
    }
    public T chooseRandom(){
        int rdn=randomInt(0,totalLength);
        for(int i=0;i<totalLength;i++){
            if(rdn<array2D[i].length){
                return array2D[i][rdn];
            }else{
                rdn-=array2D[i].length;
            }
        }
        return null;
    }
}
