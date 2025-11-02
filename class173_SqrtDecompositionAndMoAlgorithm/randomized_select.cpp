#include <iostream>
#include <vector>
#include <random>
#include <stdexcept>

/**
 * 随机化选择算法（Randomized Quick Select）
 * 算法思想：基于快速排序的思想，随机选择pivot，将数组分区，直到找到第k小的元素
 * 时间复杂度：期望 O(n)，最坏 O(n²)
 * 空间复杂度：O(log n) - 递归调用栈
 * 
 * 相关题目：
 * 1. LeetCode 215. 数组中的第K个最大元素 - https://leetcode-cn.com/problems/kth-largest-element-in-an-array/
 * 2. LintCode 5. 第k大元素 - https://www.lintcode.com/problem/5/
 * 3. CodeChef - KTHMAX - https://www.codechef.com/problems/KTHMAX
 * 4. HackerRank - Kth Largest Element - https://www.hackerrank.com/challenges/find-the-running-median/problem
 */

class RandomizedSelect {
private:
    std::random_device rd;
    std::mt19937 g;

    /**
     * 分区函数
     */
    int partition(std::vector<int>& arr, int left, int right) {
        int pivot = arr[right];
        int i = left - 1;

        for (int j = left; j < right; ++j) {
            if (arr[j] <= pivot) {
                ++i;
                std::swap(arr[i], arr[j]);
            }
        }
        
        std::swap(arr[i + 1], arr[right]);
        return i + 1;
    }

    /**
     * 随机化分区函数
     */
    int randomizedPartition(std::vector<int>& arr, int left, int right) {
        // 随机选择pivot位置
        std::uniform_int_distribution<int> dist(left, right);
        int randomIndex = dist(g);
        // 将pivot交换到末尾
        std::swap(arr[randomIndex], arr[right]);
        
        return partition(arr, left, right);
    }

    /**
     * 递归实现随机化选择
     */
    int randomizedSelectImpl(std::vector<int>& arr, int left, int right, int index) {
        if (left == right) {
            return arr[left];
        }

        // 随机选择pivot并分区
        int pivotIndex = randomizedPartition(arr, left, right);

        if (index == pivotIndex) {
            // 找到目标位置
            return arr[index];
        } else if (index < pivotIndex) {
            // 在左半部分查找
            return randomizedSelectImpl(arr, left, pivotIndex - 1, index);
        } else {
            // 在右半部分查找
            return randomizedSelectImpl(arr, pivotIndex + 1, right, index);
        }
    }

public:
    RandomizedSelect() : g(rd()) {}

    /**
     * 查找数组中第k小的元素（k从1开始计数）
     * @param arr 输入数组
     * @param k 第k小
     * @return 第k小的元素值
     */
    int findKthSmallest(std::vector<int>& arr, int k) {
        if (arr.empty()) {
            throw std::invalid_argument("数组不能为空");
        }
        if (k < 1 || k > arr.size()) {
            throw std::invalid_argument("k的取值范围应为[1, " + std::to_string(arr.size()) + "]");
        }

        return randomizedSelectImpl(arr, 0, arr.size() - 1, k - 1);
    }
};

// 测试函数
int main() {
    RandomizedSelect selector;
    std::vector<int> arr = {3, 2, 1, 5, 6, 4};
    int k = 2;
    
    try {
        int result = selector.findKthSmallest(arr, k);
        std::cout << "数组中第" << k << "小的元素是：" << result << std::endl;
        
        // 验证结果
        std::cout << "原数组：";
        for (int num : arr) {
            std::cout << num << " ";
        }
        std::cout << std::endl;
    } catch (const std::exception& e) {
        std::cerr << "错误：" << e.what() << std::endl;
    }
    
    return 0;
}