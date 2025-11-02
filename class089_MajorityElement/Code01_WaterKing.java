import java.util.Arrays;

/**
 * 水王数（Majority Element）问题
 * 
 * 题目描述：
 * 给定一个大小为n的数组nums，水王数是指在数组中出现次数大于n/2的数
 * 返回其中的水王数，如果数组不存在水王数返回-1
 * 
 * 相关题目来源：
 * 1. LeetCode 169. Majority Element - https://leetcode.com/problems/majority-element/
 * 2. LeetCode 169. 多数元素（中文版）- https://leetcode.cn/problems/majority-element/
 * 3. SPOJ MAJOR - https://www.spoj.com/problems/MAJOR/
 * 4. GeeksforGeeks Majority Element - https://www.geeksforgeeks.org/problems/majority-element-1587115620/1
 * 5. LintCode 46. Majority Element - https://www.lintcode.com/problem/46/
 * 6. HackerRank Most Frequent Element - https://www.hackerrank.com/contests/bits-hyderabad-practice-test-1/challenges/most-frequent-element
 * 7. CodeChef Find the majority element - https://www.codechef.com/practice/arrays
 * 8. POJ 2356 Find a multiple - 水王数思想的变种应用
 * 9. AtCoder Beginner Contest 155 C - Poll - 水王数的投票思想应用
 * 10. Codeforces Round #662 (Div. 2) B - Applejack and Storages - 计数相关应用
 * 11. 牛客网 NC143 - 数组中的水王数 - https://www.nowcoder.com/practice/38802713414c4852b6982410c4187dd2
 * 12. 洛谷 P1496 - 火烧赤壁 - https://www.luogu.com.cn/problem/P1496 (相关思想应用)
 * 13. USACO 2013 November Contest, Silver - Problem 3 - Election Time - https://usaco.org/index.php?page=viewproblem2&cpid=360
 *
 * 算法核心思想：
 * 使用Boyer-Moore投票算法（摩尔投票算法）：
 * 1. 维护一个候选元素(candidate)和一个计数器(count)
 * 2. 遍历数组：
 *    - 如果计数器为0，将当前元素设为候选元素，计数器设为1
 *    - 如果当前元素等于候选元素，计数器加1
 *    - 如果当前元素不等于候选元素，计数器减1
 * 3. 最后验证候选元素是否真的是水王数（出现次数大于n/2）
 *
 * 算法正确性证明：
 * 如果数组中存在水王数，那么它与其他所有元素"抵消"后仍会剩余。
 * 因为水王数出现次数超过一半，其他所有元素出现次数总和不超过一半。
 *
 * 时间复杂度分析：
 * - 时间复杂度：O(n) - 需要遍历数组两次
 *   - 第一次遍历用于找到候选元素：O(n)
 *   - 第二次遍历用于验证候选元素：O(n)
 *
 * 空间复杂度分析：
 * - 空间复杂度：O(1) - 只使用了常数级别的额外空间
 *   - 只需要两个变量来存储候选元素和计数器
 *
 * 最优解分析：
 * 该解法是最优解，因为：
 * 1. 时间复杂度已经是最优的，因为至少需要遍历一次数组才能确定每个元素的信息
 * 2. 空间复杂度也是最优的，只使用了常数级别的额外空间
 * 3. 相比使用哈希表计数的O(n)空间解法，此解法在空间上有明显优势
 *
 * 工程化考量：
 * 1. 异常处理：处理空数组、单元素数组等边界情况
 * 2. 性能优化：在实际应用中，可以根据数据分布情况优化验证步骤
 * 3. 线程安全：在多线程环境中需要注意变量的可见性和原子性
 * 4. 代码可读性：使用清晰的变量名和注释提高可维护性
 * 5. 可扩展性：算法可以扩展到寻找超过n/k次的元素
 * 6. 鲁棒性：通过第二遍验证确保结果的正确性
 *
 * 与其他领域的联系：
 * 1. 机器学习：可以用于投票集成方法中确定最终预测结果
 * 2. 数据挖掘：用于频繁模式挖掘中的频繁项发现
 * 3. 分布式系统：在分布式计算中用于数据聚合和一致性决策
 * 4. 图像处理：在图像分割和特征提取中用于确定主要特征
 * 5. 自然语言处理：用于文本分类和主题建模中的高频特征识别
 */

public class Code01_WaterKing {

    /**
     * 查找数组中的水王数（出现次数大于n/2的元素）
     * 使用Boyer-Moore投票算法，这是解决水王数问题的最优解法
     * 
     * 算法正确性证明：
     * 1. 如果数组中存在水王数，那么它与其他所有元素"抵消"后仍会剩余
     * 2. 因为水王数出现次数超过一半，其他所有元素出现次数总和不超过一半
     * 3. 在抵消过程中，水王数最终会"存活"下来
     * 
     * 时间复杂度分析：
     * - 时间复杂度：O(n) - 需要遍历数组两次
     *   - 第一次遍历用于找到候选元素：O(n)
     *   - 第二次遍历用于验证候选元素：O(n)
     * - 空间复杂度：O(1) - 只使用了常数级别的额外空间
     * 
     * 该解法是最优解，因为：
     * 1. 时间复杂度已经是最优的，因为至少需要遍历一次数组才能确定每个元素的信息
     * 2. 空间复杂度也是最优的，只使用了常数级别的额外空间
     * 3. 相比使用哈希表计数的O(n)空间解法，此解法在空间上有明显优势
     * 
     * 工程化考量：
     * 1. 异常处理：处理空数组、单元素数组等边界情况
     * 2. 性能优化：在实际应用中，可以根据数据分布情况优化验证步骤
     * 3. 线程安全：在多线程环境中需要注意变量的可见性和原子性
     * 4. 代码可读性：使用清晰的变量名和注释提高可维护性
     * 
     * @param nums 输入数组
     * @return 水王数，如果不存在则返回-1
     */
    public static int majorityElement(int[] nums) {
        // 边界情况处理：空数组或null数组
        if (nums == null || nums.length == 0) {
            return -1;  // 表示不存在水王数
        }
        
        // 第一遍遍历，使用Boyer-Moore投票算法找出候选元素
        int cand = 0;  // 候选元素
        int hp = 0;    // 血量计数器（可以理解为候选元素的"存活次数"）
        
        for (int num : nums) {
            if (hp == 0) {
                // 计数器为0，将当前元素设为候选元素
                cand = num;
                hp = 1;
            } else if (num != cand) {
                // 当前元素不等于候选元素，计数器减1（相当于抵消）
                hp--;
            } else {
                // 当前元素等于候选元素，计数器加1
                hp++;
            }
        }
        
        // 检查是否存在水王数的候选
        if (hp == 0) {
            return -1;
        }
        
        // 第二遍遍历，统计候选元素的真实出现次数
        // 复用hp变量，统计真实出现的次数
        hp = 0;
        for (int num : nums) {
            if (num == cand) {
                hp++;
            }
        }
        
        // 验证候选元素是否真的是水王数（出现次数大于n/2）
        return hp > nums.length / 2 ? cand : -1;
    }

    /**
     * 主方法：测试水王数算法
     * 包含多种测试用例，覆盖常见情况和边界情况
     */
    public static void main(String[] args) {
        // 测试用例1: 基本情况 - 水王数出现刚好超过一半
        // [3,2,3] -> 3，出现次数为2，数组长度为3，2 > 3/2
        int[] nums1 = {3, 2, 3};
        System.out.println("测试用例1 (基本情况):");
        System.out.println("输入: " + Arrays.toString(nums1));
        System.out.println("输出: " + majorityElement(nums1));
        System.out.println();
        
        // 测试用例2: 水王数出现次数接近2/3
        // [2,2,1,1,1,2,2] -> 2，出现次数为4，数组长度为7，4 > 7/2
        int[] nums2 = {2, 2, 1, 1, 1, 2, 2};
        System.out.println("测试用例2 (水王数出现次数接近2/3):");
        System.out.println("输入: " + Arrays.toString(nums2));
        System.out.println("输出: " + majorityElement(nums2));
        System.out.println();
        
        // 测试用例3: 单元素数组
        // [1] -> 1，出现次数为1，数组长度为1，1 > 1/2
        int[] nums3 = {1};
        System.out.println("测试用例3 (单元素数组):");
        System.out.println("输入: " + Arrays.toString(nums3));
        System.out.println("输出: " + majorityElement(nums3));
        System.out.println();
        
        // 测试用例4: 不存在水王数
        // [1,2,3] -> -1，没有元素出现次数超过3/2
        int[] nums4 = {1, 2, 3};
        System.out.println("测试用例4 (不存在水王数):");
        System.out.println("输入: " + Arrays.toString(nums4));
        System.out.println("输出: " + majorityElement(nums4));
        System.out.println();
        
        // 测试用例5: 所有元素都相同
        // [5,5,5,5,5] -> 5，出现次数为5，数组长度为5，5 > 5/2
        int[] nums5 = {5, 5, 5, 5, 5};
        System.out.println("测试用例5 (所有元素都相同):");
        System.out.println("输入: " + Arrays.toString(nums5));
        System.out.println("输出: " + majorityElement(nums5));
        System.out.println();
        
        // 测试用例6: 空数组
        // [] -> -1，空数组不存在水王数
        int[] nums6 = {};
        System.out.println("测试用例6 (空数组):");
        System.out.println("输入: " + Arrays.toString(nums6));
        System.out.println("输出: " + majorityElement(nums6));
        System.out.println();
        
        // 测试用例7: 偶数长度数组，水王数刚好超过一半
        // [1,1,1,2] -> 1，出现次数为3，数组长度为4，3 > 4/2
        int[] nums7 = {1, 1, 1, 2};
        System.out.println("测试用例7 (偶数长度数组，水王数刚好超过一半):");
        System.out.println("输入: " + Arrays.toString(nums7));
        System.out.println("输出: " + majorityElement(nums7));
        System.out.println();
        
        // 测试用例8: 偶数长度数组，没有元素超过一半
        // [1,1,2,2] -> -1，没有元素出现次数超过4/2
        int[] nums8 = {1, 1, 2, 2};
        System.out.println("测试用例8 (偶数长度数组，没有元素超过一半):");
        System.out.println("输入: " + Arrays.toString(nums8));
        System.out.println("输出: " + majorityElement(nums8));
    }
}