package com.yourname.plantgame.helperclass;

import java.util.ArrayList;
import java.util.List;

public class Random2DArray<T>{
    T[][] array2D;
    int totalLength=0;
    ArrayList<T> arrayList=new ArrayList<>();
    public Random2DArray(T[][] array2D){
        this.array2D=array2D;
        /*
        this.array2D=array2D.clone();
        for(int r=0;r<array2D.length;r++){
            this.array2D[r]=array2D[r].clone();
            totalLength+=array2D[r].length;
        }
         */
    }
    private static int randomInt(int a,int b){//[a,b)
        return (int) ((b-a)*Math.random())+a;
    }
    public T chooseRandom(){
        int rdn=randomInt(0,totalLength);
        for(int i=0;i<array2D.length;i++){
            if(rdn<array2D[i].length){
                return array2D[i][rdn];
            }else{
                rdn-=array2D[i].length;
            }
        }
        return null;
    }
    public T eliminateRandom(){
        if(arrayList.isEmpty()){
            for(int i=0;i<array2D.length;i++){
                arrayList.addAll(List.of(array2D[i]));
            }
        }
        return arrayList.remove(randomInt(0,arrayList.size()));
    }
}
