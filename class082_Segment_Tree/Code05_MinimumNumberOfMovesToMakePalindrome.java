package class109;

import java.util.Arrays;

// 得到回文串的最少操作次数
// 给你一个只包含小写英文字母的字符串s
// 每一次操作可以选择s中两个相邻的字符进行交换
// 返回将s变成回文串的最少操作次数
// 输入数据会确保s一定能变成一个回文串
// 测试链接 : https://leetcode.cn/problems/minimum-number-of-moves-to-make-palindrome/

/**
 * 使用树状数组和归并分治解决回文串最少操作次数问题
 * 
 * 解题思路：
 * 1. 首先确定每个字符在回文串中的位置：
 *    - 对于出现偶数次的字符，对称分布在字符串两端
 *    - 对于出现奇数次的字符，有一个会放在中间位置
 * 2. 构建位置映射数组arr，arr[i]表示原位置i的字符在回文串中的位置
 * 3. 计算arr的逆序对数量，即为需要的最少交换次数
 * 
 * 时间复杂度分析：
 * - 遍历字符串构建位置映射：O(n)
 * - 归并排序计算逆序对：O(n log n)
 * - 总时间复杂度：O(n log n)
 * 
 * 空间复杂度分析：
 * - 需要额外数组存储位置映射、树状数组等：O(n)
 * - 所以总空间复杂度为O(n)
 */
public class Code05_MinimumNumberOfMovesToMakePalindrome {

    // 最大数组长度
    public static int MAXN = 2001;

    // 字符种类数
    public static int MAXV = 26;

    // 字符串长度
    public static int n;

    // 字符数组
    public static char[] s;

    // 所有字符的位置列表
    // end[v]表示字符v最后出现的位置
    public static int[] end = new int[MAXV];
    // pre[i]表示位置i的字符在链表中的前一个位置
    public static int[] pre = new int[MAXN];

    // 树状数组，用于维护位置信息
    public static int[] tree = new int[MAXN];

    // 归并分治
    // arr[i]记录每个位置的字符最终要去哪
    public static int[] arr = new int[MAXN];
    public static int[] help = new int[MAXN];

    /**
     * 初始化各数组
     */
    public static void build() {
        Arrays.fill(end, 0, MAXV, 0);
        Arrays.fill(arr, 1, n + 1, 0);
        Arrays.fill(tree, 1, n + 1, 0);
        for (int i = 1; i <= n; i++) {
            add(i, 1);
        }
    }

    /**
     * 将字符v的位置j加入列表
     * 
     * @param v 字符
     * @param j 位置
     */
    public static void push(int v, int j) {
        pre[j] = end[v];
        end[v] = j;
    }

    /**
     * 弹出当前v字符最后的下标
     * 
     * @param v 字符
     * @return 位置
     */
    public static int pop(int v) {
        int ans = end[v];
        end[v] = pre[end[v]];
        return ans;
    }

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
        while (i <= n) {
            tree[i] += v;
            i += lowbit(i);
        }
    }

    /**
     * 查询前缀和：计算从位置1到位置i的所有元素之和
     * 
     * @param i 查询的结束位置
     * @return 前缀和
     */
    public static int sum(int i) {
        int ans = 0;
        while (i > 0) {
            ans += tree[i];
            i -= lowbit(i);
        }
        return ans;
    }

    /**
     * 计算将字符串变成回文串的最少操作次数
     * 
     * @param str 输入字符串
     * @return 最少操作次数
     */
    // 时间复杂度O(n * logn)
    public static int minMovesToMakePalindrome(String str) {
        s = str.toCharArray();
        n = s.length;
        build();
        
        // 构建每个字符的位置列表
        for (int i = 0, j = 1; i < n; i++, j++) {
            push(s[i] - 'a', j);
        }
        
        // arr[i]记录每个位置的字符最终要去哪
        for (int i = 0, l = 1, r, k; i < n; i++, l++) {
            if (arr[l] == 0) {
                // 弹出字符s[i]最后出现的位置
                r = pop(s[i] - 'a');
                if (l < r) {
                    // 计算位置l和r在回文串中的目标位置
                    k = sum(l);
                    arr[l] = k;
                    arr[r] = n - k + 1;
                } else {
                    // 奇数个字符的情况，放在中间
                    arr[l] = (1 + n) / 2;
                }
                // 在树状数组中删除位置r
                add(r, -1);
            }
        }
        // 使用归并排序计算arr的逆序对数量
        return number(1, n);
    }

    /**
     * 归并分治计算区间[l,r]内的逆序对数量
     * 
     * @param l 区间左边界
     * @param r 区间右边界
     * @return 逆序对数量
     */
    public static int number(int l, int r) {
        if (l >= r) {
            return 0;
        }
        int m = (l + r) / 2;
        return number(l, m) + number(m + 1, r) + merge(l, m, r);
    }

    /**
     * 合并两个有序数组，并统计合并过程中产生的逆序对数量
     * 
     * @param l 左半部分起始位置
     * @param m 左半部分结束位置
     * @param r 右半部分结束位置
     * @return 合并过程中产生的逆序对数量
     */
    public static int merge(int l, int m, int r) {
        int ans = 0;
        for (int i = m, j = r; i >= l; i--) {
            while (j >= m + 1 && arr[i] <= arr[j]) {
                j--;
            }
            ans += j - m;
        }
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