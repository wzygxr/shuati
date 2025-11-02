package class109;

// 逆序对数量(值域树状数组)
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
import java.util.Arrays;

/**
 * 使用值域树状数组解决逆序对问题
 * 
 * 解题思路：
 * 1. 离散化处理：由于数组元素值可能很大（1 <= arr[i] <= 10^9），但数量有限（n <= 5*10^5），
 *    所以需要离散化处理，将原数值映射到1~m的范围内
 * 2. 从右往左遍历数组，对每个元素：
 *    - 查询树状数组中比当前元素小的元素个数，即为以当前元素为第一元素的逆序对数量
 *    - 将当前元素加入树状数组
 * 
 * 时间复杂度分析：
 * - 离散化排序：O(n log n)
 * - 遍历数组，每次操作树状数组：O(n log n)
 * - 总时间复杂度：O(n log n)
 * 
 * 空间复杂度分析：
 * - 需要额外数组存储原始数据、排序数据和树状数组：O(n)
 * - 所以总空间复杂度为O(n)
 */
public class Code01_NumberOfReversePair2 {

    // 最大数组长度
    public static int MAXN = 500001;

    // 原数组
    public static int[] arr = new int[MAXN];

    // 排序数组，用于离散化
    public static int[] sort = new int[MAXN];

    // 树状数组，用于维护元素出现次数
    public static int[] tree = new int[MAXN];

    // 数组长度和离散化后数组长度
    public static int n, m;

    /**
     * lowbit函数：获取数字的二进制表示中最右边的1所代表的数值
     * 例如：x=6(110) 返回2(010)，x=12(1100) 返回4(0100)
     * 
     * @param i 输入数字
     * @return 最低位的1所代表的数值
     */
    public static int lowbit(int i) {
        return i & -i;
    }

    /**
     * 单点增加操作：在位置i上增加v
     * 
     * @param i 位置（从1开始）
     * @param v 增加的值
     */
    public static void add(int i, int v) {
        // 从位置i开始，沿着父节点路径向上更新所有相关的节点
        while (i <= m) {
            tree[i] += v;
            // 移动到父节点
            i += lowbit(i);
        }
    }

    /**
     * 查询前缀和：计算从位置1到位置i的所有元素之和
     * 
     * @param i 查询的结束位置
     * @return 前缀和
     */
    public static long sum(int i) {
        long ans = 0;
        // 从位置i开始，沿着子节点路径向下累加
        while (i > 0) {
            ans += tree[i];
            // 移动到前一个相关区间
            i -= lowbit(i);
        }
        return ans;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        in.nextToken();
        n = (int) in.nval;
        for (int i = 1; i <= n; i++) {
            in.nextToken();
            arr[i] = (int) in.nval;
            sort[i] = arr[i];
        }
        out.println(compute());
        out.flush();
        out.close();
        br.close();
    }

    /**
     * 计算逆序对数量
     * 
     * @return 逆序对总数
     */
    public static long compute() {
        // 离散化处理
        Arrays.sort(sort, 1, n + 1);
        m = 1;
        for (int i = 2; i <= n; i++) {
            // 去重
            if (sort[m] != sort[i]) {
                sort[++m] = sort[i];
            }
        }
        
        // 将原数组元素替换为离散化后的排名
        for (int i = 1; i <= n; i++) {
            arr[i] = rank(arr[i]);
        }
        
        long ans = 0;
        // 从右往左遍历数组
        for (int i = n; i >= 1; i--) {
            // 右边有多少数字是 <= 当前数值 - 1，即为以arr[i]为第一元素的逆序对数量
            ans += sum(arr[i] - 1);
            // 增加当前数字的词频
            add(arr[i], 1);
        }
        return ans;
    }

    /**
     * 给定原始值v，返回其在离散化数组中的排名（即在排序数组中的位置）
     * 
     * @param v 原始值
     * @return 排名值(排序部分1~m中的下标)
     */
    public static int rank(int v) {
        int l = 1, r = m, mid;
        int ans = 0;
        while (l <= r) {
            mid = (l + r) / 2;
            if (sort[mid] >= v) {
                ans = mid;
                r = mid - 1;
            } else {
                l = mid + 1;
            }
        }
        return ans;
    }

}