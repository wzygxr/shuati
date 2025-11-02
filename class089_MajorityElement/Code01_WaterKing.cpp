#include <stdio.h>
#include <stdlib.h>

/**
 * 出现次数大于n/2的数（水王数）
 * 给定一个大小为n的数组nums
 * 水王数是指在数组中出现次数大于n/2的数
 * 返回其中的水王数，如果数组不存在水王数返回-1
 *
 * 相关题目来源：
 * - LeetCode 169. Majority Element - https://leetcode.cn/problems/majority-element/
 * - LeetCode 169. Majority Element (英文版) - https://leetcode.com/problems/majority-element/
 * - SPOJ MAJOR - https://www.spoj.com/problems/MAJOR/
 * - GeeksforGeeks Majority Element - https://www.geeksforgeeks.org/problems/majority-element-1587115620/1
 * - LintCode 46. Majority Element - https://www.lintcode.com/problem/46/
 * - HackerRank Majority Element - https://www.hackerrank.com/challenges/majority-element/problem
 * - CodeChef MAJOR - https://www.codechef.com/problems/MAJOR
 * - UVa 11572 - Unique Snowflakes (变种问题) - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=2619
 * - Codeforces 1579E2 - Garden of the Sun (相关应用) - https://codeforces.com/contest/1579/problem/E2
 * - Project Euler 250 - 250250 (数学相关) - https://projecteuler.net/problem=250
 * - 牛客网 NC143 - 数组中的水王数 - https://www.nowcoder.com/practice/38802713414c4852b6982410c4187dd2
 * - 洛谷 P1496 - 火烧赤壁 - https://www.luogu.com.cn/problem/P1496
 *
 * 算法核心思想：Boyer-Moore投票算法
 * 
 * 算法步骤详解：
 * 1. 第一遍遍历：使用Boyer-Moore投票算法找出候选元素
 *    - 维护一个候选元素candidate和一个计数器count
 *    - 遍历数组中的每个元素：
 *      - 如果count为0，将当前元素设为候选元素，计数器设为1
 *      - 如果当前元素等于候选元素，计数器加1
 *      - 如果当前元素不等于候选元素，计数器减1
 * 2. 第二遍遍历：验证候选元素是否真的是水王数
 *    - 重新计数候选元素在数组中的真实出现次数
 *    - 如果出现次数大于数组长度的一半，则返回候选元素，否则返回-1
 * 
 * 算法正确性证明：
 * 如果数组中存在出现次数大于n/2的元素，那么其他所有元素的数量总和一定小于n/2。
 * 在投票过程中，候选元素与其他元素两两抵消，最后剩下的必然是水王数。
 * 如果不存在水王数，投票过程可能会选出一个非水王数的候选，因此必须进行第二遍验证。
 * 
 * 时间复杂度分析：
 * - 时间复杂度：O(n)，其中n是数组的长度
 *   - 第一遍遍历需要O(n)时间
 *   - 第二遍验证也需要O(n)时间
 *   - 总体时间复杂度为O(n)
 *   - 这是最优的时间复杂度，因为至少需要遍历数组一次才能获取所有元素的信息
 * 
 * 空间复杂度分析：
 * - 空间复杂度：O(1)，只使用了常数级别的额外空间
 *   - 无论输入数组的大小如何，只需要两个变量（candidate和count）
 *   - 这是最优的空间复杂度，不需要使用额外的数据结构
 * 
 * 工程化考量：
 * 1. 异常处理：函数能处理空数组和null情况
 * 2. 边界情况：正确处理单元素数组、所有元素相同等边界情况
 * 3. 线程安全：该实现是无状态的，线程安全
 * 4. 可扩展性：算法可以扩展到寻找超过n/k次的元素
 * 5. 性能优化：通过复用count变量减少内存使用
 * 6. 鲁棒性：通过第二遍验证确保结果的正确性
 * 7. 代码可读性：使用清晰的变量名和注释
 * 
 * 与其他领域的联系：
 * 1. 机器学习：可以用于投票集成方法中确定最终预测结果
 * 2. 数据挖掘：用于频繁模式挖掘中的频繁项发现
 * 3. 分布式系统：在分布式计算中用于数据聚合和一致性决策
 * 4. 图像处理：在图像分割和特征提取中用于确定主要特征
 * 5. 自然语言处理：用于文本分类和主题建模中的高频特征识别
 */

/**
 * 查找数组中的水王数（出现次数大于n/2的元素）
 * 
 * 算法思路：
 * 1. 使用Boyer-Moore投票算法找出候选元素
 * 2. 验证候选元素是否真的是水王数
 * 
 * 时间复杂度：O(n) - 需要遍历数组两次
 * 空间复杂度：O(1) - 只使用了常数级别的额外空间
 * 
 * @param nums 输入数组的指针
 * @param size 数组大小
 * @return 水王数，如果不存在则返回-1
 */
int majorityElement(int* nums, int size) {
    // 边界情况处理：空数组
    if (nums == 0 || size == 0) {
        return -1;  // 空数组不存在水王数
    }
    
    // 第一遍遍历，使用Boyer-Moore投票算法找出候选元素
    int candidate = 0;  // 候选元素
    int count = 0;      // 计数器，记录候选元素的有效次数
    
    for (int i = 0; i < size; i++) {
        if (count == 0) {
            // 当计数器为0时，需要选择一个新的候选元素
            // 这表示之前的候选元素可能已经被其他元素完全抵消
            candidate = nums[i];
            count = 1;  // 初始化计数器为1
        } else if (nums[i] != candidate) {
            // 当前元素与候选元素不同，计数器减1（相当于抵消）
            // 这种情况下，候选元素的"生命值"减少
            count--;
        } else {
            // 当前元素与候选元素相同，计数器加1
            // 这种情况下，候选元素的"生命值"增加
            count++;
        }
    }
    
    // 投票算法结束后，如果计数器为0，说明没有明显的候选元素
    if (count == 0) {
        return -1;
    }
    
    // 第二遍遍历，统计候选元素的真实出现次数
    // 注意：Boyer-Moore算法只能保证如果存在水王数，一定是候选元素
    // 但不能保证候选元素一定是水王数，因此需要验证
    count = 0;
    for (int i = 0; i < size; i++) {
        if (nums[i] == candidate) {
            count++;
        }
    }
    
    // 验证候选元素是否真的是水王数（出现次数大于n/2）
    // 这里使用的是严格大于n/2，这是题目要求
    return count > size / 2 ? candidate : -1;
}

/**
 * 打印数组的辅助函数
 * 
 * @param nums 要打印的数组指针
 * @param size 数组大小
 */
void printArray(int* nums, int size) {
    printf("[");
    for (int i = 0; i < size; i++) {
        printf("%d", nums[i]);
        if (i < size - 1) {
            printf(", ");
        }
    }
    printf("]");
}

/**
 * 测试水王数算法的函数
 * 包含多种测试用例，覆盖常见情况、边界情况和特殊情况
 */
void testMajorityElement() {
    printf("========== 水王数 (Majority Element) 算法测试 ==========\n");
    
    // 测试用例1: 基本情况 - 水王数出现刚好超过一半
    // [3,2,3] -> 3，出现次数为2，数组长度为3，2 > 3/2
    int nums1[] = {3, 2, 3};
    int size1 = sizeof(nums1) / sizeof(nums1[0]);
    printf("测试用例1 (基本情况):\n");
    printf("输入: ");
    printArray(nums1, size1);
    printf("\n预期输出: 3\n实际输出: %d\n", majorityElement(nums1, size1));
    printf("\n");
    
    // 测试用例2: 水王数出现次数接近2/3
    // [2,2,1,1,1,2,2] -> 2，出现次数为4，数组长度为7，4 > 7/2
    int nums2[] = {2, 2, 1, 1, 1, 2, 2};
    int size2 = sizeof(nums2) / sizeof(nums2[0]);
    printf("测试用例2 (水王数出现次数接近2/3):\n");
    printf("输入: ");
    printArray(nums2, size2);
    printf("\n预期输出: 2\n实际输出: %d\n", majorityElement(nums2, size2));
    printf("\n");
    
    // 测试用例3: 单元素数组
    // [1] -> 1，出现次数为1，数组长度为1，1 > 1/2
    int nums3[] = {1};
    int size3 = sizeof(nums3) / sizeof(nums3[0]);
    printf("测试用例3 (单元素数组):\n");
    printf("输入: ");
    printArray(nums3, size3);
    printf("\n预期输出: 1\n实际输出: %d\n", majorityElement(nums3, size3));
    printf("\n");
    
    // 测试用例4: 不存在水王数
    // [1,2,3] -> -1，没有元素出现次数超过3/2
    int nums4[] = {1, 2, 3};
    int size4 = sizeof(nums4) / sizeof(nums4[0]);
    printf("测试用例4 (不存在水王数):\n");
    printf("输入: ");
    printArray(nums4, size4);
    printf("\n预期输出: -1\n实际输出: %d\n", majorityElement(nums4, size4));
    printf("\n");
    
    // 测试用例5: 所有元素都相同
    // [5,5,5,5,5] -> 5，出现次数为5，数组长度为5，5 > 5/2
    int nums5[] = {5, 5, 5, 5, 5};
    int size5 = sizeof(nums5) / sizeof(nums5[0]);
    printf("测试用例5 (所有元素都相同):\n");
    printf("输入: ");
    printArray(nums5, size5);
    printf("\n预期输出: 5\n实际输出: %d\n", majorityElement(nums5, size5));
    printf("\n");
    
    // 测试用例6: 空数组
    // [] -> -1，空数组不存在水王数
    int* nums6 = NULL;
    int size6 = 0;
    printf("测试用例6 (空数组):\n");
    printf("输入: []\n");
    printf("预期输出: -1\n实际输出: %d\n", majorityElement(nums6, size6));
    printf("\n");
    
    // 测试用例7: 偶数长度数组，水王数刚好超过一半
    // [1,1,1,2] -> 1，出现次数为3，数组长度为4，3 > 4/2
    int nums7[] = {1, 1, 1, 2};
    int size7 = sizeof(nums7) / sizeof(nums7[0]);
    printf("测试用例7 (偶数长度数组，水王数刚好超过一半):\n");
    printf("输入: ");
    printArray(nums7, size7);
    printf("\n预期输出: 1\n实际输出: %d\n", majorityElement(nums7, size7));
    printf("\n");
    
    // 测试用例8: 偶数长度数组，没有元素超过一半
    // [1,1,2,2] -> -1，没有元素出现次数超过4/2
    int nums8[] = {1, 1, 2, 2};
    int size8 = sizeof(nums8) / sizeof(nums8[0]);
    printf("测试用例8 (偶数长度数组，没有元素超过一半):\n");
    printf("输入: ");
    printArray(nums8, size8);
    printf("\n预期输出: -1\n实际输出: %d\n", majorityElement(nums8, size8));
    printf("\n");
}

/**
 * 主函数：程序入口点
 * 
 * @return 程序退出状态码
 */
int main() {
    // 调用测试函数测试水王数算法
    testMajorityElement();
    
    return 0;
}