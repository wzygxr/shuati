#include <iostream>
#include <vector>
#include <queue>
#include <algorithm>
#include <stdexcept>

/**
 * LeetCode 1738: 寻找第K大的异或坐标值
 *
 * 解题思路：
 * 1. 使用二维前缀异或和计算每个坐标的异或值
 * 2. 使用最小堆维护前K个最大的异或值
 * 3. 最终堆顶元素即为第K大的异或值
 *
 * 时间复杂度：O(m*n log k)
 * 空间复杂度：O(k)
 */

class Solution {
public:
    /**
     * 计算二维矩阵中所有可能的子矩阵的异或和，并返回第K大的值
     *
     * @param matrix 二维整数数组
     * @param k 需要返回的第K大元素的索引
     * @return 所有可能的子矩阵异或和中第K大的值
     * @throws std::invalid_argument 当输入参数无效时抛出
     */
    int kthLargestValue(std::vector<std::vector<int>>& matrix, int k) {
        // 输入参数校验
        if (matrix.empty() || matrix[0].empty() || k <= 0) {
            throw std::invalid_argument("输入参数无效：矩阵不能为空且k必须为正整数");
        }

        int m = matrix.size();
        int n = matrix[0].size();

        // 检查k是否超过可能的子矩阵数量
        if (k > m * n) {
            throw std::invalid_argument("k值超过了矩阵中可能的子矩阵数量");
        }

        // 初始化前缀异或和矩阵
        std::vector<std::vector<int>> pre_xor(m + 1, std::vector<int>(n + 1, 0));

        // 最小堆，用于维护前K个最大的异或值
        // C++的优先队列默认是最大堆，所以需要使用greater来实现最小堆
        std::priority_queue<int, std::vector<int>, std::greater<int>> min_heap;

        // 计算前缀异或和并维护最小堆
        for (int i = 1; i <= m; ++i) {
            for (int j = 1; j <= n; ++j) {
                // 计算当前位置的前缀异或和
                // pre_xor[i][j] 表示从 (0,0) 到 (i-1,j-1) 的子矩阵的异或和
                pre_xor[i][j] = pre_xor[i-1][j-1] ^ pre_xor[i-1][j] ^ pre_xor[i][j-1] ^ matrix[i-1][j-1];

                // 将当前异或值添加到最小堆
                min_heap.push(pre_xor[i][j]);

                // 如果堆的大小超过k，弹出最小元素
                if (min_heap.size() > k) {
                    min_heap.pop();
                }
            }
        }

        // 堆顶元素即为第K大的异或值
        return min_heap.top();
    }
};

class AlternativeSolution {
public:
    /**
     * 计算二维矩阵中所有可能的子矩阵的异或和，并返回第K大的值
     * 使用收集所有值并排序的方法
     *
     * @param matrix 二维整数数组
     * @param k 需要返回的第K大元素的索引
     * @return 所有可能的子矩阵异或和中第K大的值
     * @throws std::invalid_argument 当输入参数无效时抛出
     */
    int kthLargestValue(std::vector<std::vector<int>>& matrix, int k) {
        // 输入参数校验
        if (matrix.empty() || matrix[0].empty() || k <= 0) {
            throw std::invalid_argument("输入参数无效：矩阵不能为空且k必须为正整数");
        }

        int m = matrix.size();
        int n = matrix[0].size();

        // 检查k是否超过可能的子矩阵数量
        if (k > m * n) {
            throw std::invalid_argument("k值超过了矩阵中可能的子矩阵数量");
        }

        // 初始化前缀异或和矩阵
        std::vector<std::vector<int>> pre_xor(m + 1, std::vector<int>(n + 1, 0));

        // 存储所有异或值的向量
        std::vector<int> values;
        values.reserve(m * n);  // 预分配空间以提高效率

        // 计算前缀异或和并收集所有异或值
        for (int i = 1; i <= m; ++i) {
            for (int j = 1; j <= n; ++j) {
                pre_xor[i][j] = pre_xor[i-1][j-1] ^ pre_xor[i-1][j] ^ pre_xor[i][j-1] ^ matrix[i-1][j-1];
                values.push_back(pre_xor[i][j]);
            }
        }

        // 排序并返回第K大的值
        std::sort(values.begin(), values.end(), std::greater<int>());
        return values[k - 1];
    }
};

class OptimizedSolution {
public:
    /**
     * 计算二维矩阵中所有可能的子矩阵的异或和，并返回第K大的值（优化版本）
     * 使用一维数组优化空间复杂度
     *
     * @param matrix 二维整数数组
     * @param k 需要返回的第K大元素的索引
     * @return 所有可能的子矩阵异或和中第K大的值
     * @throws std::invalid_argument 当输入参数无效时抛出
     */
    int kthLargestValue(std::vector<std::vector<int>>& matrix, int k) {
        // 输入参数校验
        if (matrix.empty() || matrix[0].empty() || k <= 0) {
            throw std::invalid_argument("输入参数无效：矩阵不能为空且k必须为正整数");
        }

        int m = matrix.size();
        int n = matrix[0].size();

        // 检查k是否超过可能的子矩阵数量
        if (k > m * n) {
            throw std::invalid_argument("k值超过了矩阵中可能的子矩阵数量");
        }

        // 使用一维数组存储当前行的前缀异或和，节省空间
        std::vector<int> prev_row(n + 1, 0);
        std::priority_queue<int, std::vector<int>, std::greater<int>> min_heap;

        // 遍历每一行
        for (int i = 0; i < m; ++i) {
            // 当前行的前缀异或和
            std::vector<int> curr_row(n + 1, 0);
            for (int j = 0; j < n; ++j) {
                // 计算当前位置的前缀异或和
                curr_row[j + 1] = curr_row[j] ^ prev_row[j + 1] ^ prev_row[j] ^ matrix[i][j];

                // 维护最小堆
                min_heap.push(curr_row[j + 1]);
                if (min_heap.size() > k) {
                    min_heap.pop();
                }
            }

            // 更新前一行的前缀异或和
            prev_row = std::move(curr_row);  // 使用移动语义提高效率
        }

        return min_heap.top();
    }
};

/**
 * 测试寻找第K大的异或坐标值的函数
 */
void test_kth_largest_value() {
    // 测试用例1：基本用例
    std::vector<std::vector<int>> matrix1 = {{5, 2}, {1, 6}};
    int k1 = 1;
    std::cout << "测试用例1：" << std::endl;
    std::cout << "矩阵: [[5, 2], [1, 6]], k: " << k1 << std::endl;
    Solution solution;
    try {
        int result1 = solution.kthLargestValue(matrix1, k1);
        std::cout << "结果: " << result1 << std::endl;
        std::cout << "预期结果: 7, 测试" << (result1 == 7 ? "通过" : "失败") << std::endl;
    } catch (const std::exception& e) {
        std::cout << "异常: " << e.what() << std::endl;
    }
    std::cout << std::endl;

    // 测试用例2：k=2
    int k2 = 2;
    std::cout << "测试用例2：" << std::endl;
    std::cout << "矩阵: [[5, 2], [1, 6]], k: " << k2 << std::endl;
    try {
        int result2 = solution.kthLargestValue(matrix1, k2);
        std::cout << "结果: " << result2 << std::endl;
        std::cout << "预期结果: 7, 测试" << (result2 == 7 ? "通过" : "失败") << std::endl;
    } catch (const std::exception& e) {
        std::cout << "异常: " << e.what() << std::endl;
    }
    std::cout << std::endl;

    // 测试用例3：k=3
    int k3 = 3;
    std::cout << "测试用例3：" << std::endl;
    std::cout << "矩阵: [[5, 2], [1, 6]], k: " << k3 << std::endl;
    try {
        int result3 = solution.kthLargestValue(matrix1, k3);
        std::cout << "结果: " << result3 << std::endl;
        std::cout << "预期结果: 6, 测试" << (result3 == 6 ? "通过" : "失败") << std::endl;
    } catch (const std::exception& e) {
        std::cout << "异常: " << e.what() << std::endl;
    }
    std::cout << std::endl;

    // 测试用例4：3x3矩阵
    std::vector<std::vector<int>> matrix4 = {{10, 8, 6}, {3, 5, 7}, {4, 9, 2}};
    int k4 = 4;
    AlternativeSolution solution4;
    std::cout << "测试用例4：" << std::endl;
    std::cout << "3x3矩阵, k: " << k4 << std::endl;
    try {
        int result4 = solution4.kthLargestValue(matrix4, k4);
        std::cout << "结果: " << result4 << std::endl;
    } catch (const std::exception& e) {
        std::cout << "异常: " << e.what() << std::endl;
    }
    std::cout << std::endl;

    // 测试用例5：单元素矩阵
    std::vector<std::vector<int>> matrix5 = {{42}};
    int k5 = 1;
    OptimizedSolution solution5;
    std::cout << "测试用例5：" << std::endl;
    std::cout << "单元素矩阵, k: " << k5 << std::endl;
    try {
        int result5 = solution5.kthLargestValue(matrix5, k5);
        std::cout << "结果: " << result5 << std::endl;
        std::cout << "预期结果: 42, 测试" << (result5 == 42 ? "通过" : "失败") << std::endl;
    } catch (const std::exception& e) {
        std::cout << "异常: " << e.what() << std::endl;
    }
    std::cout << std::endl;

    // 测试异常处理
    try {
        std::vector<std::vector<int>> empty_matrix;
        solution.kthLargestValue(empty_matrix, 1);
        std::cout << "测试用例6：空矩阵异常处理 - 失败" << std::endl;
    } catch (const std::invalid_argument& e) {
        std::cout << "测试用例6：空矩阵异常处理 - 通过" << std::endl;
    } catch (const std::exception& e) {
        std::cout << "测试用例6：捕获到意外异常 - " << e.what() << std::endl;
    }

    try {
        solution.kthLargestValue(matrix1, 0);
        std::cout << "测试用例7：k=0异常处理 - 失败" << std::endl;
    } catch (const std::invalid_argument& e) {
        std::cout << "测试用例7：k=0异常处理 - 通过" << std::endl;
    } catch (const std::exception& e) {
        std::cout << "测试用例7：捕获到意外异常 - " << e.what() << std::endl;
    }
}

int main() {
    test_kth_largest_value();
    return 0;
}