#include <iostream>
#include <vector>
#include <random>
#include <algorithm>

/**
 * Fisher-Yates 洗牌算法
 * 算法思想：从数组末尾开始，将当前位置与之前的随机位置交换，确保每个元素都有相同的概率出现在任意位置
 * 时间复杂度：O(n)
 * 空间复杂度：O(1) - 原地洗牌
 * 
 * 相关题目：
 * 1. LeetCode 384. 打乱数组 - https://leetcode-cn.com/problems/shuffle-an-array/
 * 2. LintCode 1423. 随机洗牌 - https://www.lintcode.com/problem/1423/
 * 3. CodeChef - SHUFFLE - https://www.codechef.com/problems/SHUFFLE
 */

class FisherYatesShuffle {
private:
    std::random_device rd;
    std::mt19937 g;

public:
    FisherYatesShuffle() : g(rd()) {}

    /**
     * Fisher-Yates 洗牌算法实现
     * @param array 需要洗牌的数组
     */
    void shuffle(std::vector<int>& array) {
        if (array.size() <= 1) {
            return;
        }

        // 从后往前遍历数组
        for (int i = array.size() - 1; i > 0; --i) {
            // 生成 [0, i] 范围内的随机索引
            std::uniform_int_distribution<int> dist(0, i);
            int j = dist(g);
            // 交换 array[i] 和 array[j]
            std::swap(array[i], array[j]);
        }
    }
};

// 打印数组
void printArray(const std::vector<int>& array) {
    for (int num : array) {
        std::cout << num << " ";
    }
    std::cout << std::endl;
}

// 测试函数
int main() {
    FisherYatesShuffle shuffler;
    std::vector<int> array = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    
    std::cout << "原始数组：" << std::endl;
    printArray(array);
    
    shuffler.shuffle(array);
    
    std::cout << "洗牌后数组：" << std::endl;
    printArray(array);
    
    return 0;
}