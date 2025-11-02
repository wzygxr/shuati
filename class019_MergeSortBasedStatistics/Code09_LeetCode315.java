package class022;

import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================================
 * 题目9: LeetCode 315 - 计算右侧小于当前元素的个数
 * ============================================================================
 * 
 * 题目来源: LeetCode
 * 题目链接: https://leetcode.cn/problems/count-of-smaller-numbers-after-self/
 * 难度级别: 困难
 * 
 * 问题描述:
 * 给你一个整数数组 nums ，按要求返回一个新数组 counts 。
 * 数组 counts 有该性质：counts[i] 的值是 nums[i] 右侧小于 nums[i] 的元素的数量。
 * 
 * 示例输入输出:
 * 输入: nums = [5,2,6,1]
 * 输出: [2,1,1,0]
 * 解释:
 * - 对于nums[0]=5，右侧小于5的元素有2和1，所以counts[0]=2
 * - 对于nums[1]=2，右侧小于2的元素有1，所以counts[1]=1
 * - 对于nums[2]=6，右侧小于6的元素有1，所以counts[2]=1
 * - 对于nums[3]=1，右侧没有元素，所以counts[3]=0
 * 
 * ============================================================================
 * 核心算法思想: 归并排序+索引映射
 * ============================================================================
 * 
 * 方法1: 暴力解法 (不推荐)
 * - 思路: 双重循环检查每个元素右侧有多少元素比它小
 * - 时间复杂度: O(N^2) - 双重循环
 * - 空间复杂度: O(N) - 结果数组
 * - 问题: 数据量大时超时
 * 
 * 方法2: 归并排序思想 (最优解) ★★★★★
 * - 核心洞察: 
 *   1. 利用归并排序的分治过程统计元素之间的大小关系
 *   2. 关键挑战: 归并排序会改变元素顺序，需要维护原始索引
 *   3. 解决方案: 创建索引数组，对索引进行排序而非对值排序
 * 
 * - 归并排序过程:
 *   1. 分治: 将数组不断二分，直到只有一个元素
 *   2. 统计: 在合并过程中统计右侧小于当前元素的数量
 *   3. 合并: 按值的大小合并两个有序子数组
 * 
 * - 统计右侧小元素的关键步骤:
 *   - 当右子数组中的元素被选中时，不会对左侧元素产生影响
 *   - 当左子数组中的元素被选中时，右子数组中剩余的所有元素都是比它小的
 *   - 因此，每次选中左子数组元素时，需要记录右侧已经统计过的元素数量
 * 
 * - 时间复杂度详细计算:
 *   T(n) = 2T(n/2) + O(n)  [Master定理 case 2]
 *   = O(n log n)
 *   - 递归深度: log n
 *   - 每层合并与统计: O(n)
 * 
 * - 空间复杂度详细计算:
 *   S(n) = O(n) + O(log n)
 *   - O(n): 辅助数组、索引数组、结果数组
 *   - O(log n): 递归调用栈
 *   总计: O(n)
 * 
 * - 是否最优解: ★ 是 ★
 *   理由: 基于比较的算法下界为O(n log n)，本算法已达到最优
 * 
 * ============================================================================
 * 相关题目列表 (同类算法)
 * ============================================================================
 * 1. LeetCode 493 - 翻转对
 *    https://leetcode.cn/problems/reverse-pairs/
 *    问题：统计满足 nums[i] > 2*nums[j] 且 i < j 的对的数量
 *    解法：归并排序过程中使用双指针统计跨越左右区间的翻转对
 * 
 * 2. LeetCode 327 - 区间和的个数
 *    https://leetcode.cn/problems/count-of-range-sum/
 *    问题：统计区间和在[lower, upper]范围内的区间个数
 *    解法：前缀和+归并排序，统计满足条件的前缀和对
 * 
 * 3. 剑指Offer 51 / LCR 170 - 数组中的逆序对
 *    https://leetcode.cn/problems/shu-zu-zhong-de-ni-xu-dui-lcof/
 *    问题：统计数组中逆序对的总数
 *    解法：归并排序过程中统计逆序对数量
 * 
 * 4. LeetCode 1365 - 有多少小于当前数字的数字
 *    https://leetcode.cn/problems/how-many-numbers-are-smaller-than-the-current-number/
 *    问题：统计数组中小于当前数字的数字个数（全数组范围）
 *    解法：排序+哈希表映射
 * 
 * 5. POJ 2299 - Ultra-QuickSort
 *    http://poj.org/problem?id=2299
 *    问题：计算将数组排序所需的最小交换次数（即逆序对数量）
 *    解法：归并排序统计逆序对
 * 
 * 6. HackerRank - Merge Sort: Counting Inversions
 *    https://www.hackerrank.com/challenges/merge-sort/problem
 *    问题：统计逆序对数量
 *    解法：归并排序统计逆序对
 * 
 * 7. 牛客网 - 计算右侧小于当前元素的个数
 *    问题：与LeetCode 315相同
 *    解法：归并排序+索引映射
 * 
 * 8. 杭电OJ - 1394
 *    http://acm.hdu.edu.cn/showproblem.php?pid=1394
 *    问题：将数组循环左移，求所有可能排列中的最小逆序对数量
 *    解法：归并排序+逆序对性质分析
 * 
 * 9. 洛谷 P1908 - 逆序对
 *    https://www.luogu.com.cn/problem/P1908
 *    问题：统计数组中逆序对的总数
 *    解法：归并排序统计逆序对
 * 
 * 10. SPOJ - INVCNT
 *    https://www.spoj.com/problems/INVCNT/
 *    问题：统计逆序对数量
 *    解法：归并排序统计逆序对
 * 
 * 这些题目虽然具体形式不同，但核心思想都是利用归并排序的分治特性，在合并过程中高效统计满足特定条件的元素对数量。
 */
public class Code09_LeetCode315 {
    
    /**
     * Pair类用于在归并排序过程中同时保存元素值和原始索引
     * 这样可以在排序过程中维护原始数组的位置信息
     */
    static class Pair {
        int val; // 元素值
        int idx; // 元素在原始数组中的索引
        
        /**
         * 构造函数
         * @param val 元素值
         * @param idx 原始索引
         */
        Pair(int val, int idx) {
            this.val = val;
            this.idx = idx;
        }
    }
    
    // 常量定义
    public static final int MAXN = 100001;
    
    // 全局数组，避免多次内存分配
    public static Pair[] arr = new Pair[MAXN];  // 原数组，保存值和索引
    public static Pair[] help = new Pair[MAXN]; // 辅助数组，用于归并过程
    public static int[] count = new int[MAXN];  // 结果数组，存储每个元素右侧小于它的元素个数
    
    /**
     * 计算右侧小于当前元素的个数的主方法
     * 
     * @param nums 输入整数数组
     * @return 包含每个元素右侧小于它的元素个数的列表
     * 
     * Java语言特性注意事项:
     * 1. 数组是引用类型，作为参数传递时传递的是引用副本
     * 2. 使用全局数组避免了频繁的内存分配和释放
     * 3. 方法使用静态修饰符，可以直接通过类名调用
     */
    public static List<Integer> countSmaller(int[] nums) {
        int n = nums.length;
        // 初始化Pair数组，将元素值和索引关联起来
        for (int i = 0; i < n; i++) {
            arr[i] = new Pair(nums[i], i);
        }
        
        // 重置计数数组
        for (int i = 0; i < n; i++) {
            count[i] = 0;
        }
        
        // 执行归并排序并统计
        mergeSort(0, n - 1);
        
        // 转换结果格式
        List<Integer> result = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            result.add(count[i]);
        }
        return result;
    }
    
    /**
     * 归并排序的核心方法
     * 
     * @param l 当前处理区间的左边界
     * @param r 当前处理区间的右边界
     * 
     * 算法分析:
     * - 递归实现归并排序
     * - 时间复杂度：O(n log n)
     * - 空间复杂度：O(n)
     */
    public static void mergeSort(int l, int r) {
        // 基本情况：区间只有一个元素时直接返回
        if (l == r) {
            return;
        }
        
        // 计算中间位置，使用这种方式避免大整数溢出
        int m = l + (r - l) / 2;
        
        // 分治：递归处理左右子区间
        mergeSort(l, m);
        mergeSort(m + 1, r);
        
        // 合并两个有序子区间，同时统计结果
        merge(l, m, r);
    }
    
    /**
     * 合并两个有序子数组，并在合并过程中统计右侧小于当前元素的个数
     * 
     * @param l 当前处理区间的左边界
     * @param m 当前处理区间的中点
     * @param r 当前处理区间的右边界
     * 
     * 核心统计逻辑:
     * - 当从左侧子数组选取元素时，右侧子数组中已处理的元素都小于该元素
     * - 因此需要将右侧已处理的元素数量累加到该元素的计数中
     */
    public static void merge(int l, int m, int r) {
        int i = l;  // 辅助数组的指针
        int a = l;  // 左侧子数组的指针
        int b = m + 1;  // 右侧子数组的指针
        
        // 合并两个子数组，同时统计右侧小于当前元素的个数
        while (a <= m && b <= r) {
            if (arr[a].val <= arr[b].val) {
                // 当左侧元素小于等于右侧元素时，右侧数组中已经处理的元素都小于当前左侧元素
                // 统计右侧已处理的元素数量：b - (m + 1) = b - m - 1
                count[arr[a].idx] += (b - m - 1);
                help[i++] = arr[a++];
            } else {
                // 当右侧元素小于左侧元素时，将右侧元素放入辅助数组
                // 此时不更新计数，因为左侧元素还未被处理
                help[i++] = arr[b++];
            }
        }
        
        // 处理左侧剩余元素
        while (a <= m) {
            // 左侧剩余元素的右侧所有元素都小于它
            count[arr[a].idx] += (b - m - 1);
            help[i++] = arr[a++];
        }
        
        // 处理右侧剩余元素
        while (b <= r) {
            help[i++] = arr[b++];
        }
        
        // 将辅助数组内容复制回原数组
        for (i = l; i <= r; i++) {
            arr[i] = help[i];
        }
    }
    
    /**
     * 主方法，用于测试
     */
    public static void main(String[] args) {
        // 测试用例1: 基本情况
        int[] test1 = {5, 2, 6, 1};
        System.out.println("输入: " + java.util.Arrays.toString(test1));
        System.out.println("输出: " + countSmaller(test1)); // 预期输出: [2, 1, 1, 0]
        
        // 测试用例2: 空数组
        int[] test2 = {};
        System.out.println("输入: " + java.util.Arrays.toString(test2));
        System.out.println("输出: " + countSmaller(test2)); // 预期输出: []
        
        // 测试用例3: 单元素数组
        int[] test3 = {1};
        System.out.println("输入: " + java.util.Arrays.toString(test3));
        System.out.println("输出: " + countSmaller(test3)); // 预期输出: [0]
        
        // 测试用例4: 逆序数组
        int[] test4 = {5, 4, 3, 2, 1};
        System.out.println("输入: " + java.util.Arrays.toString(test4));
        System.out.println("输出: " + countSmaller(test4)); // 预期输出: [4, 3, 2, 1, 0]
        
        // 测试用例5: 有序数组
        int[] test5 = {1, 2, 3, 4, 5};
        System.out.println("输入: " + java.util.Arrays.toString(test5));
        System.out.println("输出: " + countSmaller(test5)); // 预期输出: [0, 0, 0, 0, 0]
        
        // 测试用例6: 重复元素
        int[] test6 = {2, 2, 2};
        System.out.println("输入: " + java.util.Arrays.toString(test6));
        System.out.println("输出: " + countSmaller(test6)); // 预期输出: [0, 0, 0]
        
        // 测试用例7: 包含负数
        int[] test7 = {-1, -2, 3, -4, 5};
        System.out.println("输入: " + java.util.Arrays.toString(test7));
        System.out.println("输出: " + countSmaller(test7)); // 预期输出: [2, 1, 1, 0, 0]
    }
    
    /*
     * ============================================================================
     * Java语言特有关注事项
     * ============================================================================
     * 1. 静态成员变量的使用：
     *    - 使用静态数组避免了频繁的内存分配和释放
     *    - 但需注意线程安全问题，多线程环境下可能需要额外的同步措施
     *    - 静态变量在类加载时初始化，在类卸载时销毁
     * 
     * 2. 内存优化：
     *    - 预分配固定大小的数组（MAXN）减少了动态扩容的开销
     *    - 使用循环初始化数组，避免使用Arrays.fill()方法
     *    - ArrayList的初始容量设置为数组大小，避免扩容开销
     * 
     * 3. 整数类型和溢出：
     *    - Java的int类型是32位有符号整数，范围为-2^31到2^31-1
     *    - 计算中间点使用l + (r - l) / 2避免整数溢出
     *    - 对于非常大的数组，索引可能超出int范围，需要考虑使用long类型
     * 
     * 4. 递归深度控制：
     *    - Java的默认递归深度限制约为1000-2000层（依赖JVM实现）
     *    - 对于大规模数据，可以通过JVM参数-Xss调整栈大小
     *    - 对于极端情况，考虑实现迭代版本的归并排序
     * 
     * 5. 异常处理：
     *    - Java中可以使用try-catch块处理可能的异常
     *    - 可以添加对null输入、极大数组等边界情况的检查
     * 
     * 6. 性能优化：
     *    - 对于小规模子数组（如长度<10），可以使用插入排序提高效率
     *    - 可以添加判断条件，当arr[m].val <= arr[m+1].val时，子数组已有序，跳过合并
     *    - 减少对象创建和垃圾回收压力，提高性能
     */
    
    /*
     * ============================================================================
     * 工程化考量
     * ============================================================================
     * 1. 异常处理：
     *    - 可以添加对null输入的检查：if (nums == null) { return new ArrayList<>(); }
     *    - 对于极大数组，可以添加数组长度检查，避免超出MAXN限制
     *    - 可以抛出IllegalArgumentException处理无效输入
     * 
     * 2. 线程安全：
     *    - 当前实现使用静态变量，不是线程安全的
     *    - 改进方案1：将静态变量改为方法局部变量，每次调用时创建新数组
     *    - 改进方案2：使用ThreadLocal存储线程局部变量
     *    - 改进方案3：添加同步机制（synchronized或ReentrantLock）
     * 
     * 3. 测试与质量保证：
     *    - 已提供多种测试用例，覆盖常见场景
     *    - 建议使用JUnit框架编写自动化单元测试
     *    - 可以添加性能测试，验证算法在大规模数据下的表现
     * 
     * 4. 代码可读性：
     *    - 使用了清晰的变量命名和方法命名
     *    - 添加了详细的注释说明算法逻辑
     *    - 代码结构清晰，易于理解和维护
     * 
     * 5. 可扩展性：
     *    - 可以将算法扩展为泛型版本，支持任意Comparable类型
     *    - 可以将核心逻辑抽象为策略模式，方便替换不同的统计策略
     *    - 可以添加缓存机制，避免重复计算
     * 
     * 6. 内存管理：
     *    - 使用预分配的静态数组减少GC压力
     *    - 对于超大规模数据，可以考虑分块处理或使用堆外内存
     * 
     * 7. 性能调优：
     *    - 可以使用JMH（Java Microbenchmark Harness）进行性能基准测试
     *    - 可以通过JIT编译器优化，避免热点路径上的对象创建
     *    - 考虑使用并行流或Fork/Join框架实现并行归并排序
     *
     * 8. 代码优化建议：
     *    - 对于大规模数据，考虑使用更高效的排序算法或数据结构，如二叉搜索树、树状数组等
     *    - 可以优化合并过程，减少不必要的对象创建
     *    - 对于特定应用场景，可以考虑启发式优化
     *
     * 9. 跨平台兼容性：
     *    - Java具有良好的跨平台特性，代码可以在不同的操作系统和JVM上运行
     *    - 但需注意不同JVM实现可能在性能和行为上有所差异
     *
     * 10. 代码安全性：
     *    - 避免使用全局变量存储状态，减少副作用
     *    - 对外部输入进行严格验证，防止注入攻击
     *    - 注意数组边界检查，避免越界异常
     */
}