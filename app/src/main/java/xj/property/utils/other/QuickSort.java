package xj.property.utils.other;

/**
 * Created by Administrator on 2015/5/22.
 */
public class QuickSort {
//  public static   void quickSort(int[] a, int low, int high) {
//        p = get(a, low, high);
//        quickSort(a, low, p-1);
//        quickSort(a, p+1, high);
//    }
//
//    int get(int[] a, int low, int high){
//        compare = a[low];
//
//        while(low < high){ //无论如何置换, 被置换的都包含compare的值
//            while(low<high && a[high]>=compare)
//                high--;
//
//            //在 low<high 的情况下找到a[high]<compare并置换
//            temp = a[low];
//            a[low] = a[high];
//            a[high] = temp;
//
//            while(low<high && a[low]<=compare)
//                low++;
//
//            //在 low<high 的情况下找到a[low]>compare并置换
//            temp = a[low];
//            a[low] = a[high];
//            a[high] = temp;
//        }
//        return low; //while(low==hight)停止循环, 并返回枢轴位置
//    }
}
