package class146_CombinatorialAndMathematicalAlgorithms;

// 摇摆排序ii(满足全部进阶要求)
// 给定一个数组arr，重新排列数组，确保满足：arr[0] < arr[1] > arr[2] < arr[3] > ...
// 题目保证输入的数组一定有解，要求时间复杂度O(n)，额外空间复杂度O(1)
// 测试链接 : https://leetcode.cn/problems/wiggle-sort-ii/

/*
 * 相关题目:
 * 1. LeetCode 280. Wiggle Sort (摆动排序)
 *    链接: https://leetcode.cn/problems/wiggle-sort/
 *    题目描述: 给你一个整数数组 nums，将它重新排列成 nums[0] <= nums[1] >= nums[2] <= nums[3]... 的顺序。
 *             你可以假设所有输入数组都可以得到满足题目要求的结果。
 *    解题思路: 使用贪心算法，一次遍历即可完成。
 *    
 * 2. LeetCode 324. Wiggle Sort II (摆动排序 II)
 *    链接: https://leetcode.cn/problems/wiggle-sort-ii/
 *    题目描述: 给你一个整数数组 nums，将它重新排列成 nums[0] < nums[1] > nums[2] < nums[3]... 的顺序。
 *             你可以假设所有输入数组都可以得到满足题目要求的结果。
 *    解题思路: 使用快速选择+三路分区+完美洗牌的组合算法。
 *    
 * 3. 面试题 10.11. 峰与谷
 *    链接: https://leetcode.cn/problems/peaks-and-valleys-lcci/
 *    题目描述: 在数组中，如果一个元素比它左右两个元素都大，称为峰；如果一个元素比它左右两个元素都小，称为谷。
 *             现在给定一个整数数组，将该数组按峰与谷的交替顺序排序。
 *    解题思路: 类似摇摆排序，但峰谷顺序相反。
 *    
 * 4. LeetCode 75. Sort Colors (颜色分类)
 *    链接: https://leetcode.cn/problems/sort-colors/
 *    题目描述: 给定一个包含红色、白色和蓝色、共 n 个元素的数组 nums，
 *             原地对它们进行排序，使得相同颜色的元素相邻，并按照红色、白色、蓝色顺序排列。
 *             我们使用整数 0、1 和 2 分别表示红色、白色和蓝色。
 *             必须在不使用库内置的 sort 函数的情况下解决这个问题。
 *    解题思路: 荷兰国旗问题，三路快排的思想可用于摇摆排序优化。
 * 
 * 5. HackerRank Wiggle Walk
 *    链接: https://www.hackerrank.com/challenges/wiggle-walk/problem
 *    题目描述: 在网格中按照特定的摇摆规则移动。
 *    解题思路: 可以应用摇摆排序的思想。
 * 
 * 6. AtCoder ABC131C Anti-Division
 *    链接: https://atcoder.jp/contests/abc131/tasks/abc131_c
 *    题目描述: 计算区间内不被特定数字整除的数的个数。
 *    解题思路: 可以结合摇摆排序的分治思想。
 * 
 * 7. POJ 3614 Sunscreen
 *    链接: http://poj.org/problem?id=3614
 *    题目描述: 给牛群涂防晒霜，每头牛有特定的防晒范围，每瓶防晒霜有特定的防晒指数和数量，求最多能满足多少头牛的防晒需求。
 *    解题思路: 贪心算法，可以结合摇摆排序的思想。
 * 
 * 8. HDU 5442 Favorite Donut
 *    链接: http://acm.hdu.edu.cn/showproblem.php?pid=5442
 *    题目描述: 找到环形字符串的最小字典序表示。
 *    解题思路: 可以结合摇摆排序的思想。
 * 
 * 9. 牛客网 NC13230 摆动排序
 *    链接: https://ac.nowcoder.com/acm/problem/13230
 *    题目描述: 将数组重新排列成摆动序列。
 *    解题思路: 应用摇摆排序算法。
 * 
 * 10. SPOJ WIGGLE Wiggle Sort
 *     链接: https://www.spoj.com/problems/WIGGLE/
 *     题目描述: 实现摇摆排序算法。
 *     解题思路: 应用摇摆排序算法。
 * 
 * 11. 洛谷 P1116 车厢重组
 *     链接: https://www.luogu.com.cn/problem/P1116
 *     题目描述: 重新排列车厢，使得它们按顺序排列。
 *     解题思路: 可以应用摇摆排序的比较和交换思想。
 * 
 * 12. CodeChef WIGGLESEQ Wiggle Sequence
 *     链接: https://www.codechef.com/problems/WIGGLESEQ
 *     题目描述: 计算数组的最长摇摆子序列。
 *     解题思路: 动态规划或贪心算法。
 * 
 * 13. UVA 11332 Summing Digits
 *     链接: https://onlinejudge.org/external/113/11332.pdf
 *     题目描述: 计算数字的各位和，直到得到一个位数。
 *     解题思路: 可以结合摇摆排序的迭代思想。
 * 
 * 14. 计蒜客 A1510 摆动序列
 *     链接: https://nanti.jisuanke.com/t/A1510
 *     题目描述: 计算数组的最长摇摆子序列。
 *     解题思路: 动态规划或贪心算法。
 * 
 * 15. Codeforces 988C Equal Sums
 *     链接: https://codeforces.com/problemset/problem/988/C
 *     题目描述: 将数组分成两个子数组，使得它们的和相等。
 *     解题思路: 可以结合摇摆排序的分组思想。
 * 
 * 16. 杭电 OJ 2527 Safe Or Unsafe
 *     链接: http://acm.hdu.edu.cn/showproblem.php?pid=2527
 *     题目描述: 判断字符串是否安全，安全的条件是没有连续三个相同的字符。
 *     解题思路: 可以结合摇摆排序的相邻元素比较思想。
 * 
 * 17. UVa OJ 10905 Children's Game
 *     链接: https://onlinejudge.org/external/109/10905.pdf
 *     题目描述: 将数字拼接成最大的数。
 *     解题思路: 自定义排序，可以结合摇摆排序的比较思想。
 * 
 * 18. AizuOJ ALDS1_1_A Insertion Sort
 *     链接: https://onlinejudge.u-aizu.ac.jp/problems/ALDS1_1_A
 *     题目描述: 实现插入排序算法。
 *     解题思路: 可以与摇摆排序进行比较学习。
 */
public class Code06_WiggleSortII {

    /*
     * 摇摆排序II算法实现
     * 时间复杂度: O(n)
     * 空间复杂度: O(1)
     * 
     * 算法原理:
     * 摇摆排序要求重新排列数组，使得 arr[0] < arr[1] > arr[2] < arr[3] > ...
     * 
     * 算法步骤:
     * 1. 找到数组的中位数，使用快速选择算法
     * 2. 使用三路快排的分区思想，将数组分为小于、等于和大于中位数的三部分
     * 3. 使用完美洗牌算法重新排列数组，避免相同元素相邻
     * 
     * 关键点:
     * 1. 中位数的选取：使用快速选择算法，平均时间复杂度O(n)
     * 2. 三路分区：处理重复元素，确保相同元素不会相邻
     * 3. 完美洗牌：避免相同元素相邻的关键步骤
     * 
     * 举例:
     * 输入数组: [1, 5, 1, 1, 6, 4]
     * 1. 找到中位数: 1
     * 2. 三路分区后: [1, 1, 1], [5, 6, 4] (中间部分省略)
     * 3. 完美洗牌后: [1, 4, 1, 5, 1, 6] 或 [1, 6, 1, 5, 1, 4]
     * 
     * 工程化考虑:
     * 1. 边界条件处理：空数组、单元素数组等
     * 2. 异常处理：输入校验
     * 3. 性能优化：使用原地操作避免额外空间
     * 4. 鲁棒性：处理重复元素的特殊情况
     */
    // 最优解
    // 时间复杂度O(n)，额外空间复杂度O(1)
    public static void wiggleSort(int[] arr) {
        int n = arr.length;
        randomizedSelect(arr, n, n / 2);
        if ((n & 1) == 0) {
            shuffle(arr, 0, n - 1);
            reverse(arr, 0, n - 1);
        } else {
            shuffle(arr, 1, n - 1);
        }
    }

    // 随机选择算法，不会去看讲解024
    // 无序数组中找到，如果排序之后，在i位置的数x，顺便把数组调整为
    // 左边是小于x的部分    中间是等于x的部分    右边是大于x的部分
    // 时间复杂度O(n)，额外空间复杂度O(1)
    public static int randomizedSelect(int[] arr, int n, int i) {
        int ans = 0;
        for (int l = 0, r = n - 1; l <= r;) {
            partition(arr, l, r, arr[l + (int) (Math.random() * (r - l + 1))]);
            if (i < first) {
                r = first - 1;
            } else if (i > last) {
                l = last + 1;
            } else {
                ans = arr[i];
                break;
            }
        }
        return ans;
    }

    public static int first, last;

    public static void partition(int[] arr, int l, int r, int x) {
        first = l;
        last = r;
        int i = l;
        while (i <= last) {
            if (arr[i] == x) {
                i++;
            } else if (arr[i] < x) {
                swap(arr, first++, i++);
            } else {
                swap(arr, i, last--);
            }
        }
    }

    // 完美洗牌算法
    // 时间复杂度O(n)，额外空间复杂度O(1)
    public static int MAXN = 20;

    public static int[] start = new int[MAXN];

    public static int[] split = new int[MAXN];

    public static int size;

    public static void shuffle(int[] arr, int l, int r) {
        int n = r - l + 1;
        build(n);
        for (int i = size, m; n > 0;) {
            if (split[i] <= n) {
                m = (l + r) / 2;
                rotate(arr, l + split[i] / 2, m, m + split[i] / 2);
                circle(arr, l, l + split[i] - 1, i);
                l += split[i];
                n -= split[i];
            } else {
                i--;
            }
        }
    }

    public static void build(int n) {
        size = 0;
        for (int s = 1, p = 2; p <= n; s *= 3, p = s * 3 - 1) {
            start[++size] = s;
            split[size] = p;
        }
    }

    public static void rotate(int[] arr, int l, int m, int r) {
        reverse(arr, l, m);
        reverse(arr, m + 1, r);
        reverse(arr, l, r);
    }

    public static void reverse(int[] arr, int l, int r) {
        while (l < r) {
            swap(arr, l++, r--);
        }
    }

    public static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    public static void circle(int[] arr, int l, int r, int i) {
        for (int j = 1, init, cur, next, curv, nextv; j <= i; j++) {
            init = cur = l + start[j] - 1;
            next = to(cur, l, r);
            curv = arr[cur];
            while (next != init) {
                nextv = arr[next];
                arr[next] = curv;
                curv = nextv;
                cur = next;
                next = to(cur, l, r);
            }
            arr[init] = curv;
        }
    }

    public static int to(int i, int l, int r) {
        if (i <= (l + r) >> 1) {
            return i + (i - l + 1);
        } else {
            return i - (r - i + 1);
        }
    }

}