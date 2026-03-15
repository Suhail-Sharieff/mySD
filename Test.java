import java.util.Arrays;

public class Test {

    public static void main(String[] args) {
        


        long cost[]={5,12,3,8,6,10,4};
        int isc[]=  {0,0,0,0,0,0,0};
        int nw=2;
        int nc=0;

        
        System.out.println(function(cost,isc,nw,nc));


    }
    static long function(long cost[],int isc[],int nw,int nc){
        
        long low=0,high=Arrays.stream(cost).sum()<<1l;

        long ans=-1;

        while(low<=high){
            long lim=(low+high)>>1l;
            long sum=0;
            long nParts=0;
            long nCursed=0;
            System.out.println("--------for "+lim+"-------");
            for(int i=0;i<cost.length;i++){
                sum+=cost[i];
                long ifTake=sum;
                if(isc[i]==1) nCursed++;
                if(nCursed>nc) ifTake<<=1l;

                if(ifTake<=lim){
                    if(ifTake<lim) continue;
                    else{
                        //eq
                        nParts++;
                        sum=0;
                        nCursed=0;
                        // System.out.print(cost[i]+"->");
                    }
                }else{
                    nParts++;
                    sum=cost[i];
                    nCursed=isc[i];
                    // System.out.print(cost[i]+"->");
                }
            }
            if(sum!=0) nParts++;
            System.out.println();
            if(nParts<=nw){
                ans=lim;
                high=lim-1;
            }else low=lim+1;

        }
        return ans;

    }   
}