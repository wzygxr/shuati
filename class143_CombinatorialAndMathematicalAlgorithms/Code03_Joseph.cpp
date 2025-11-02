#include <iostream>
#include <vector>
#include <string>
#include <stdexcept>
#include <chrono>
#include <cstdio>

/**
 * 约瑟夫环问题算法实现
 * 经典约瑟夫问题：n个人围成一圈，每次数到k的人出列，求最后剩下的人的位置
 * 
 * 适用场景：
 * - 循环淘汰问题
 * - 环状结构中的选择问题
 * - 递推算法的典型应用
 * 
 * 相关题目:
 * 1. LeetCode 390. Elimination Game (消除游戏)
 *    链接: https://leetcode.cn/problems/elimination-game/
 * 2. LeetCode 1823. Find the Winner of the Circular Game (找出游戏的获胜者)
 *    链接: https://leetcode.cn/problems/find-the-winner-of-the-circular-game/
 * 3. POJ 1012 Joseph
 *    链接: http://poj.org/problem?id=1012
 * 4. POJ 2886 Who Gets the Most Candies?
 *    链接: http://poj.org/problem?id=2886
 * 5. Luogu P1996 约瑟夫问题
 *    链接: https://www.luogu.com.cn/problem/P1996
 */
class Josephus {
public:
    /**
     * 使用递推公式求解约瑟夫环问题的最优解
     * 时间复杂度: O(n)
     * 空间复杂度: O(1)
     * 
     * 递推公式：f(n,k) = (f(n-1,k) + k) % n
     * 其中f(n,k)表示n个人数k时最后剩下的人的索引（从0开始）
     * 这里返回的是从1开始计数的结果
     * 
     * @param n 总人数
     * @param k 每次数到k的人出列
     * @return 最后剩下的人的位置（从1开始计数）
     * @throws std::invalid_argument 当参数不合法时抛出异常
     */
    static int compute(int n, int k) {
        // 参数校验
        if (n <= 0 || k <= 0) {
            throw std::invalid_argument("n和k必须为正整数");
        }
        
        // 特殊情况优化：当k=1时，最后剩下的是第n个人
        if (k == 1) {
            return n;
        }
        
        // 特殊情况优化：当n=1时，只剩一个人，就是他自己
        if (n == 1) {
            return 1;
        }
        
        // 使用递推法求解
        // 初始条件：当只有1个人时，位置就是1
        int ans = 1;
        
        // 从2个人开始递推，直到n个人
        for (int c = 2; c <= n; c++) {
            // 递推公式：新位置 = (旧位置 + k - 1) % 当前人数 + 1
            // +k-1是因为数到第k个人，-1是为了从0开始计算
            // %c是为了处理环形结构
            // +1是为了将结果转换回从1开始计数
            ans = (ans + k - 1) % c + 1;
        }
        
        return ans;
    }
    
    /**
     * 使用递推公式（索引从0开始）
     * 这是标准的约瑟夫环递推公式实现
     * 
     * @param n 总人数
     * @param k 每次数到k的人出列
     * @return 最后剩下的人的索引（从0开始）
     * @throws std::invalid_argument 当参数不合法时抛出异常
     */
    static int josephus0Based(int n, int k) {
        if (n <= 0 || k <= 0) {
            throw std::invalid_argument("n和k必须为正整数");
        }
        
        int res = 0; // f(1) = 0
        for (int i = 2; i <= n; i++) {
            res = (res + k) % i;
        }
        return res;
    }
    
    /**
     * 使用模拟法求解约瑟夫环问题
     * 适用于小数据量，直观但效率较低
     * 时间复杂度: O(nk)
     * 空间复杂度: O(n)
     * 
     * @param n 总人数
     * @param k 每次数到k的人出列
     * @return 最后剩下的人的位置（从1开始计数）
     * @throws std::invalid_argument 当参数不合法时抛出异常
     */
    static int simulate(int n, int k) {
        if (n <= 0 || k <= 0) {
            throw std::invalid_argument("n和k必须为正整数");
        }
        
        // 创建列表存储所有人的位置
        std::vector<int> people;
        for (int i = 1; i <= n; i++) {
            people.push_back(i);
        }
        
        int index = 0; // 当前开始计数的位置
        
        // 不断删除出列的人，直到只剩一个人
        while (people.size() > 1) {
            // 计算要删除的人的位置
            // (index + k - 1) % people.size() 确保在列表范围内循环
            index = (index + k - 1) % people.size();
            people.erase(people.begin() + index);
        }
        
        // 返回最后剩下的人的位置
        return people[0];
    }
    
    /**
     * 递归求解约瑟夫环问题
     * 适用于理解算法原理，但对于大n可能导致栈溢出
     * 时间复杂度: O(n)
     * 空间复杂度: O(n) 递归调用栈
     * 
     * @param n 总人数
     * @param k 每次数到k的人出列
     * @return 最后剩下的人的索引（从0开始）
     * @throws std::invalid_argument 当参数不合法时抛出异常
     */
    static int recursive(int n, int k) {
        if (n <= 0 || k <= 0) {
            throw std::invalid_argument("n和k必须为正整数");
        }
        
        // 基本情况：只有一个人时，索引为0
        if (n == 1) {
            return 0;
        }
        
        // 递推公式：f(n,k) = (f(n-1,k) + k) % n
        return (recursive(n - 1, k) + k) % n;
    }
    
    /**
     * 优化的约瑟夫环算法，当k远小于n时可以进一步优化
     * 时间复杂度: O(n) 最坏情况，但在k较小的情况下性能更好
     * 
     * @param n 总人数
     * @param k 每次数到k的人出列
     * @return 最后剩下的人的位置（从1开始计数）
     * @throws std::invalid_argument 当参数不合法时抛出异常
     */
    static int optimizedJosephus(int n, int k) {
        if (n <= 0 || k <= 0) {
            throw std::invalid_argument("n和k必须为正整数");
        }
        
        // 当k=1时，最后剩下的是第n个人
        if (k == 1) {
            return n;
        }
        
        // 当k较大时，使用标准递推
        if (k > n) {
            return compute(n, k % n == 0 ? n : k % n);
        }
        
        int res = 0;
        for (int i = 2; i <= n; i++) {
            if (res + k < i) {
                // 可以跳过多个步骤
                res += k;
            } else {
                res = (res + k) % i;
            }
        }
        
        return res + 1; // 转换为从1开始计数
    }
    
    /**
     * 输出完整的出列顺序
     * 
     * @param n 总人数
     * @param k 每次数到k的人出列
     * @return 出列顺序的vector
     * @throws std::invalid_argument 当参数不合法时抛出异常
     */
    static std::vector<int> getEliminationOrder(int n, int k) {
        if (n <= 0 || k <= 0) {
            throw std::invalid_argument("n和k必须为正整数");
        }
        
        std::vector<int> people;
        for (int i = 1; i <= n; i++) {
            people.push_back(i);
        }
        
        std::vector<int> order;
        order.reserve(n);
        int index = 0;
        
        for (int i = 0; i < n; i++) {
            index = (index + k - 1) % people.size();
            order.push_back(people[index]);
            people.erase(people.begin() + index);
        }
        
        return order;
    }
};

// 为了保持与原代码的兼容性，实现经典LeetCode 1823题的解法
int find_the_winner(int n, int k) {
    /**
     * LeetCode 1823. Find the Winner of the Circular Game 的解决方案
     * 题目描述：n个朋友围成一个圈，从朋友1开始，顺时针方向数到第k个人离开圈。
     * 继续从下一个人开始，重复这一过程，直到只剩下一个人。
     * 
     * @param n 朋友的数量
     * @param k 数到第k个人离开
     * @return 最后剩下的朋友的编号（从1开始计数）
     */
    return Josephus::compute(n, k);
}

// 为了兼容题目测试要求，保留原始的compute函数接口
int compute(int n, int k) {
    return Josephus::compute(n, k);
}

/**
 * 主函数，读取输入并计算约瑟夫环问题的解
 * 支持处理大规模输入
 * 
 * 算法题解总结：
 * 1. 约瑟夫环问题是典型的递推问题，最优解法是O(n)时间复杂度的递推公式
 * 2. 适用于环形结构中的淘汰问题，如游戏、调度算法等场景
 * 3. 常见的变种包括双向淘汰（如LeetCode 390）、带权重淘汰等
 * 4. 递推公式的核心思想是从子问题的解推导出原问题的解
 * 5. 实际应用中需要注意边界条件处理和大规模数据的性能优化
 */
int main() {
    try {
        int n, k;
        // 使用scanf进行更高效的输入
        printf("请输入总人数n和报数k: ");
        scanf("%d %d", &n, &k);
        
        // 计算并输出结果
        auto startTime = std::chrono::high_resolution_clock::now();
        int result = Josephus::compute(n, k);
        auto endTime = std::chrono::high_resolution_clock::now();
        
        printf("最后剩下的人的位置是: %d\n", result);
        
        // 输出性能信息
        auto duration = std::chrono::duration_cast<std::chrono::microseconds>(endTime - startTime);
        printf("计算耗时: %.3f ms\n", duration.count() / 1000.0);
        
        // 测试其他实现方法
        printf("\n其他实现方法结果对比:\n");
        printf("递推法结果(从0开始): %d\n", Josephus::josephus0Based(n, k));
        
        // 只在小数据量时测试模拟法，避免超时
        if (n <= 10000) {
            startTime = std::chrono::high_resolution_clock::now();
            int simulateResult = Josephus::simulate(n, k);
            endTime = std::chrono::high_resolution_clock::now();
            duration = std::chrono::duration_cast<std::chrono::microseconds>(endTime - startTime);
            printf("模拟法结果: %d，耗时: %.3f ms\n", simulateResult, duration.count() / 1000.0);
        } else {
            printf("模拟法对于大数据量n=%d可能耗时较长，跳过测试\n", n);
        }
        
        // 只在小数据量时测试递归法，避免栈溢出
        if (n <= 10000) {
            try {
                startTime = std::chrono::high_resolution_clock::now();
                int recursiveResult = Josephus::recursive(n, k) + 1; // 转换为从1开始
                endTime = std::chrono::high_resolution_clock::now();
                duration = std::chrono::duration_cast<std::chrono::microseconds>(endTime - startTime);
                printf("递归法结果: %d，耗时: %.3f ms\n", recursiveResult, duration.count() / 1000.0);
            } catch (const std::exception& e) {
                printf("递归法执行出错: %s\n", e.what());
            }
        } else {
            printf("递归法对于大数据量n=%d可能导致栈溢出，跳过测试\n", n);
        }
        
        startTime = std::chrono::high_resolution_clock::now();
        int optimizedResult = Josephus::optimizedJosephus(n, k);
        endTime = std::chrono::high_resolution_clock::now();
        duration = std::chrono::duration_cast<std::chrono::microseconds>(endTime - startTime);
        printf("优化法结果: %d，耗时: %.3f ms\n", optimizedResult, duration.count() / 1000.0);
        
        // 只在小数据量时输出出列顺序
        if (n <= 100) {
            printf("\n出列顺序: ");
            std::vector<int> order = Josephus::getEliminationOrder(n, k);
            for (size_t i = 0; i < order.size(); i++) {
                printf("%d", order[i]);
                if (i < order.size() - 1) {
                    printf(", ");
                }
            }
            printf("\n");
        }
        
        // 输出时间复杂度分析
        printf("\n时间复杂度分析:\n");
        printf("递推法: O(n) 时间，O(1) 空间\n");
        printf("模拟法: O(nk) 时间，O(n) 空间\n");
        printf("递归法: O(n) 时间，O(n) 空间（递归栈）\n");
        printf("优化法: O(n) 时间（最坏情况），但在k较小时性能更好\n");
        
    } catch (const std::invalid_argument& e) {
        // 处理非法参数
        fprintf(stderr, "错误: %s\n", e.what());
        return 1;
    } catch (const std::exception& e) {
        // 处理其他异常
        fprintf(stderr, "发生错误: %s\n", e.what());
        return 1;
    }
    
    return 0;
}

// 编译命令: g++ -std=c++11 Code03_Joseph.cpp -o Code03_Joseph