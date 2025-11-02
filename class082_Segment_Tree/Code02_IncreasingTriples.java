package class109;

// 升序三元组数量
// 给定一个数组arr，如果i < j < k且arr[i] < arr[j] < arr[k]
// 那么称(i, j, k)为一个升序三元组
// 返回arr中升序三元组的数量
// 测试链接 : https://www.luogu.com.cn/problem/P1637
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
 * 使用树状数组解决升序三元组问题
 * 
 * 解题思路：
 * 1. 对于每个元素arr[i]，我们需要知道：
 *    - 在它左边有多少个元素小于它（形成升序一元组）
 *    - 在它左边有多少个元素能与它组成升序二元组
 * 2. 使用两个树状数组：
 *    - tree1[i]维护以数值i结尾的升序一元组数量（即小于i的元素个数）
 *    - tree2[i]维护以数值i结尾的升序二元组数量
 * 3. 遍历数组，对每个元素：
 *    - 查询tree2中比当前元素小的元素个数，即为以当前元素为结尾的升序三元组数量
 *    - 更新tree1中当前元素的计数
 *    - 查询tree1中比当前元素小的元素个数，更新tree2中当前元素的计数
 * 
 * 时间复杂度分析：
 * - 离散化排序：O(n log n)
 * - 遍历数组，每次操作树状数组：O(n log n)
 * - 总时间复杂度：O(n log n)
 * 
 * 空间复杂度分析：
 * - 需要额外数组存储原始数据、排序数据和两个树状数组：O(n)
 * - 所以总空间复杂度为O(n)
 */
public class Code02_IncreasingTriples {

    // 最大数组长度
    public static int MAXN = 30001;

    // 原数组
    public static int[] arr = new int[MAXN];

    // 排序数组，用于离散化
    public static int[] sort = new int[MAXN];

    // 维护信息 : 课上讲的up1数组
    // tree1不是up1数组，是up1数组的树状数组
    // tree1[i]表示值小于等于i的元素个数（升序一元组数量）
    public static long[] tree1 = new long[MAXN];

    // 维护信息 : 课上讲的up2数组
    // tree2不是up2数组，是up2数组的树状数组
    // tree2[i]表示以值i结尾的升序二元组数量
    public static long[] tree2 = new long[MAXN];

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
     * @param tree 树状数组
     * @param i 位置（从1开始）
     * @param c 增加的值
     */
    public static void add(long[] tree, int i, long c) {
        // 从位置i开始，沿着父节点路径向上更新所有相关的节点
        while (i <= m) {
            tree[i] += c;
            // 移动到父节点
            i += lowbit(i);
        }
    }

    /**
     * 查询前缀和：计算从位置1到位置i的所有元素之和
     * 
     * @param tree 树状数组
     * @param i 查询的结束位置
     * @return 前缀和
     */
    public static long sum(long[] tree, int i) {
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
     * 计算升序三元组数量
     * 
     * @return 升序三元组总数
     */
    // 时间复杂度O(n * logn)
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
        // 遍历数组，对每个元素计算以它为结尾的升序三元组数量
        for (int i = 1; i <= n; i++) {
            // 查询以当前值做结尾的升序三元组数量
            // 即查询右方有多少数字能与当前数字组成升序二元组
            ans += sum(tree2, arr[i] - 1);
            
            // 更新以当前值做结尾的升序一元组数量（单个元素）
            add(tree1, arr[i], 1);
            
            // 更新以当前值做结尾的升序二元组数量
            // 即当前元素与左方比它小的元素组成的二元组数量
            add(tree2, arr[i], sum(tree1, arr[i] - 1));
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