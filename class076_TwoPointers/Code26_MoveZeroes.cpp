#include <iostream>
#include <vector>
#include <chrono>

/**
 * LeetCode 283. 移动零 (Move Zeroes)
 * 
 * 题目描述:
 * 给定一个数组 nums，编写一个函数将所有 0 移动到数组的末尾，同时保持非零元素的相对顺序。
 * 请注意 ，必须在不复制数组的情况下原地对数组进行操作。
 * 
 * 示例1:
 * 输入: nums = [0,1,0,3,12]
 * 输出: [1,3,12,0,0]
 * 
 * 示例2:
 * 输入: nums = [0]
 * 输出: [0]
 * 
 * 提示:
 * 1 <= nums.length <= 10^4
 * -2^31 <= nums[i] <= 2^31 - 1
 * 
 * 题目链接: https://leetcode.com/problems/move-zeroes/
 * 
 * 解题思路:
 * 这道题可以使用双指针的方法来解决：
 * 
 * 方法一（双指针）：
 * 1. 使用两个指针 slow 和 fast，初始都指向0
 * 2. fast指针用于遍历整个数组，当遇到非零元素时，将其移动到 slow 指向的位置，然后 slow++
 * 3. 遍历结束后，将 slow 到数组末尾的所有元素都设为0
 * 
 * 方法二（优化的双指针）：
 * 1. 同样使用两个指针 slow 和 fast，初始都指向0
 * 2. 当 fast 指向非零元素时，如果 slow != fast，交换 nums[slow] 和 nums[fast]，然后 slow++
 * 3. 这种方法避免了不必要的赋值操作，只在需要时进行交换
 * 
 * 最优解是方法二，时间复杂度 O(n)，空间复杂度 O(1)。
 */

/**
 * 解法一: 双指针基础版
 * 
 * @param nums 输入数组
 * @throws std::invalid_argument 如果输入数组为null
 */
void moveZeroes(std::vector<int>& nums) {
    // 参数校验
    if (nums.empty()) {
        return; // 空数组不需要操作
    }
    
    // 慢指针，指向当前应该放置非零元素的位置
    int slow = 0;
    
    // 快指针遍历整个数组
    for (int fast = 0; fast < nums.size(); fast++) {
        // 如果当前元素不为0，将其移到慢指针位置
        if (nums[fast] != 0) {
            nums[slow] = nums[fast];
            slow++;
        }
    }
    
    // 将slow之后的所有元素都设为0
    for (int i = slow; i < nums.size(); i++) {
        nums[i] = 0;
    }
}

/**
 * 解法二: 优化的双指针（最优解）
 * 
 * @param nums 输入数组
 */
void moveZeroesOptimized(std::vector<int>& nums) {
    // 参数校验
    if (nums.empty()) {
        return; // 空数组不需要操作
    }
    
    // 慢指针，指向当前应该放置非零元素的位置
    int slow = 0;
    
    // 快指针遍历整个数组
    for (int fast = 0; fast < nums.size(); fast++) {
        // 如果当前元素不为0，且slow和fast不同，交换它们
        if (nums[fast] != 0) {
            if (slow != fast) {
                std::swap(nums[slow], nums[fast]);
            }
            slow++;
        }
    }
}

/**
 * 解法三: 一次遍历的另一种实现
 * 
 * @param nums 输入数组
 */
void moveZeroesOnePass(std::vector<int>& nums) {
    // 参数校验
    if (nums.empty()) {
        return; // 空数组不需要操作
    }
    
    int lastNonZeroFoundAt = 0;
    
    // 将所有非零元素移到数组前面
    for (int i = 0; i < nums.size(); i++) {
        if (nums[i] != 0) {
            nums[lastNonZeroFoundAt++] = nums[i];
        }
    }
    
    // 填充剩余位置为0
    for (int i = lastNonZeroFoundAt; i < nums.size(); i++) {
        nums[i] = 0;
    }
}

/**
 * 打印向量内容
 */
void printVector(const std::vector<int>& vec) {
    std::cout << "[";
    for (size_t i = 0; i < vec.size(); i++) {
        std::cout << vec[i];
        if (i < vec.size() - 1) {
            std::cout << ", ";
        }
    }
    std::cout << "]";
}

/**
 * 验证结果是否正确
 */
bool validateResult(const std::vector<int>& original, const std::vector<int>& expected) {
    if (original.size() != expected.size()) {
        return false;
    }
    
    // 验证所有元素相等
    for (size_t i = 0; i < original.size(); i++) {
        if (original[i] != expected[i]) {
            return false;
        }
    }
    
    // 验证0都在末尾
    bool zeroStarted = false;
    for (int num : original) {
        if (zeroStarted && num != 0) {
            return false; // 发现0后面有非零元素
        }
        if (num == 0) {
            zeroStarted = true;
        }
    }
    
    return true;
}

/**
 * 测试函数
 */
void test() {
    // 测试用例1
    std::vector<int> nums1 = {0, 1, 0, 3, 12};
    std::vector<int> expected1 = {1, 3, 12, 0, 0};
    std::cout << "测试用例1:\n";
    std::cout << "原数组: ";
    printVector(nums1);
    std::cout << std::endl;
    moveZeroesOptimized(nums1);
    std::cout << "处理后: ";
    printVector(nums1);
    std::cout << std::endl;
    std::cout << "验证结果: " << (validateResult(nums1, expected1) ? "true" : "false") << std::endl;
    std::cout << std::endl;
    
    // 测试用例2
    std::vector<int> nums2 = {0};
    std::vector<int> expected2 = {0};
    std::cout << "测试用例2:\n";
    std::cout << "原数组: ";
    printVector(nums2);
    std::cout << std::endl;
    moveZeroesOptimized(nums2);
    std::cout << "处理后: ";
    printVector(nums2);
    std::cout << std::endl;
    std::cout << "验证结果: " << (validateResult(nums2, expected2) ? "true" : "false") << std::endl;
    std::cout << std::endl;
    
    // 测试用例3 - 边界情况：没有零元素
    std::vector<int> nums3 = {1, 2, 3, 4, 5};
    std::vector<int> expected3 = {1, 2, 3, 4, 5};
    std::cout << "测试用例3（无零元素）:\n";
    std::cout << "原数组: ";
    printVector(nums3);
    std::cout << std::endl;
    moveZeroesOptimized(nums3);
    std::cout << "处理后: ";
    printVector(nums3);
    std::cout << std::endl;
    std::cout << "验证结果: " << (validateResult(nums3, expected3) ? "true" : "false") << std::endl;
    std::cout << std::endl;
    
    // 测试用例4 - 边界情况：所有元素都是零
    std::vector<int> nums4 = {0, 0, 0, 0, 0};
    std::vector<int> expected4 = {0, 0, 0, 0, 0};
    std::cout << "测试用例4（全零元素）:\n";
    std::cout << "原数组: ";
    printVector(nums4);
    std::cout << std::endl;
    moveZeroesOptimized(nums4);
    std::cout << "处理后: ";
    printVector(nums4);
    std::cout << std::endl;
    std::cout << "验证结果: " << (validateResult(nums4, expected4) ? "true" : "false") << std::endl;
    std::cout << std::endl;
    
    // 测试用例5 - 边界情况：零元素在开头
    std::vector<int> nums5 = {0, 0, 1, 2, 3};
    std::vector<int> expected5 = {1, 2, 3, 0, 0};
    std::cout << "测试用例5（零在开头）:\n";
    std::cout << "原数组: ";
    printVector(nums5);
    std::cout << std::endl;
    moveZeroesOptimized(nums5);
    std::cout << "处理后: ";
    printVector(nums5);
    std::cout << std::endl;
    std::cout << "验证结果: " << (validateResult(nums5, expected5) ? "true" : "false") << std::endl;
    std::cout << std::endl;
    
    // 测试用例6 - 边界情况：零元素在末尾
    std::vector<int> nums6 = {1, 2, 3, 0, 0};
    std::vector<int> expected6 = {1, 2, 3, 0, 0};
    std::cout << "测试用例6（零在末尾）:\n";
    std::cout << "原数组: ";
    printVector(nums6);
    std::cout << std::endl;
    moveZeroesOptimized(nums6);
    std::cout << "处理后: ";
    printVector(nums6);
    std::cout << std::endl;
    std::cout << "验证结果: " << (validateResult(nums6, expected6) ? "true" : "false") << std::endl;
    std::cout << std::endl;
}

/**
 * 性能测试
 */
void performanceTest() {
    // 创建一个大数组进行性能测试
    const size_t size = 1000000;
    std::vector<int> largeArray(size);
    
    // 生成测试数据：交替放置0和1
    for (size_t i = 0; i < size; i++) {
        largeArray[i] = i % 2;  // 0,1,0,1,...
    }
    
    // 测试解法一的性能
    std::vector<int> array1 = largeArray;
    auto start = std::chrono::high_resolution_clock::now();
    moveZeroes(array1);
    auto end = std::chrono::high_resolution_clock::now();
    auto duration = std::chrono::duration_cast<std::chrono::milliseconds>(end - start).count();
    std::cout << "解法一（基础双指针）耗时: " << duration << "ms" << std::endl;
    
    // 测试解法二的性能
    std::vector<int> array2 = largeArray;
    start = std::chrono::high_resolution_clock::now();
    moveZeroesOptimized(array2);
    end = std::chrono::high_resolution_clock::now();
    duration = std::chrono::duration_cast<std::chrono::milliseconds>(end - start).count();
    std::cout << "解法二（优化双指针）耗时: " << duration << "ms" << std::endl;
    
    // 测试解法三的性能
    std::vector<int> array3 = largeArray;
    start = std::chrono::high_resolution_clock::now();
    moveZeroesOnePass(array3);
    end = std::chrono::high_resolution_clock::now();
    duration = std::chrono::duration_cast<std::chrono::milliseconds>(end - start).count();
    std::cout << "解法三（一次遍历）耗时: " << duration << "ms" << std::endl;
    
    // 验证所有解法结果一致
    bool resultsConsistent = true;
    for (size_t i = 0; i < size; i++) {
        if (array1[i] != array2[i] || array1[i] != array3[i]) {
            resultsConsistent = false;
            break;
        }
    }
    std::cout << "所有解法结果一致: " << (resultsConsistent ? "true" : "false") << std::endl;
    
    // 验证结果正确性
    bool isCorrect = true;
    bool zeroStarted = false;
    for (int num : array1) {
        if (zeroStarted && num != 0) {
            isCorrect = false;
            break;
        }
        if (num == 0) {
            zeroStarted = true;
        }
    }
    std::cout << "结果正确: " << (isCorrect ? "true" : "false") << std::endl;
}

int main() {
    std::cout << "=== 测试用例 ===" << std::endl;
    test();
    
    std::cout << "=== 性能测试 ===" << std::endl;
    performanceTest();
    
    return 0;
}