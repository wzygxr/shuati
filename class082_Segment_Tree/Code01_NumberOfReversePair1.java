package class109;

// 逆序对数量(归并分治)
// 给定一个长度为n的数组arr
// 如果 i < j 且 arr[i] > arr[j]
// 那么(i,j)就是一个逆序对
// 求arr中逆序对的数量
// 1 <= n <= 5 * 10^5
// 1 <= arr[i] <= 10^9
// 测试链接 : https://www.luogu.com.cn/problem/P1908
// 请同学们务必参考如下代码中关于输入、输出的处理
// 这是输入输出处理效率很高的写法
// 提交以下的code，提交时请把类名改成"Main"，可以直接通过

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

/**
 * 使用归并排序思想解决逆序对问题
 * 
 * 解题思路：
 * 1. 采用分治思想，将数组不断二分，直到只有一个元素
 * 2. 在合并过程中统计逆序对数量
 * 3. 对于左半部分[l...m]和右半部分[m+1...r]：
 *    - 当arr[i] > arr[j]时，说明从i到m的所有元素都大于arr[j]，产生m-i+1个逆序对
 *    - 当arr[i] <= arr[j]时，不产生逆序对
 * 
 * 时间复杂度分析：
 * - 归并排序的时间复杂度为O(n log n)
 * - 每一层递归都会遍历所有元素，共log n层
 * - 所以总时间复杂度为O(n log n)
 * 
 * 空间复杂度分析：
 * - 需要额外的help数组存储临时数据，空间复杂度为O(n)
 * - 递归调用栈的深度为O(log n)
 * - 所以总空间复杂度为O(n)
 */
public class Code01_NumberOfReversePair1 {

    // 最大数组长度
    public static int MAXN = 500001;

    // 原数组
    public static int[] arr = new int[MAXN];

    // 辅助数组，用于归并过程
    public static int[] help = new int[MAXN];

    // 数组长度
    public static int n;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        in.nextToken();
        n = (int) in.nval;
        for (int i = 1; i <= n; i++) {
            in.nextToken();
            arr[i] = (int) in.nval;
        }
        out.println(compute());
        out.flush();
        out.close();
        br.close();
    }

    /**
     * 计算逆序对数量的入口方法
     * 
     * @return 逆序对总数
     */
    public static long compute() {
        return f(1, n);
    }

    /**
     * 分治计算区间[l,r]内的逆序对数量
     * 
     * @param l 区间左边界（包含）
     * @param r 区间右边界（包含）
     * @return 区间内逆序对数量
     */
    public static long f(int l, int r) {
        // 递归终止条件：只有一个元素时，没有逆序对
        if (l == r) {
            return 0;
        }
        // 二分中点
        int m = (l + r) / 2;
        // 递归计算左半部分、右半部分的逆序对数量，再加上合并时产生的逆序对数量
        return f(l, m) + f(m + 1, r) + merge(l, m, r);
    }

    /**
     * 合并两个有序数组，并统计合并过程中产生的逆序对数量
     * 
     * @param l 左半部分起始位置
     * @param m 左半部分结束位置
     * @param r 右半部分结束位置
     * @return 合并过程中产生的逆序对数量
     */
    public static long merge(int l, int m, int r) {
        // i来自l.....m（左半部分）
        // j来自m+1...r（右半部分）
        // 统计有多少逆序对
        
        // 逆序对数量
        long ans = 0;
        
        // 从后往前比较，统计左半部分中大于右半部分元素的个数
        for (int i = m, j = r; i >= l; i--) {
            // 找到右半部分中第一个小于arr[i]的元素位置
            while (j >= m + 1 && arr[i] <= arr[j]) {
                j--;
            }
            // 此时j指向右半部分中最后一个满足arr[i] > arr[j]的元素位置
            // 从m+1到j的所有元素都与arr[i]构成逆序对
            ans += j - m;
        }
        
        // 左右部分合并，整体变有序，归并排序的过程
        int i = l;
        int a = l;
        int b = m + 1;
        while (a <= m && b <= r) {
            help[i++] = arr[a] <= arr[b] ? arr[a++] : arr[b++];
        }
        while (a <= m) {
            help[i++] = arr[a++];
        }
        while (b <= r) {
            help[i++] = arr[b++];
        }
        for (i = l; i <= r; i++) {
            arr[i] = help[i];
        }
        return ans;
    }

}