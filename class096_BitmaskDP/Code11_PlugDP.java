package class081.补充题目;

import java.util.*;

// 插头DP (Plug DP) 专题
// 插头DP是一种用于处理网格路径、回路等问题的状态压缩动态规划
// 题目来源: HDU, POJ等OJ平台
//
// 核心思想:
// 使用轮廓线技术，记录当前处理位置的轮廓线状态
// 状态表示当前轮廓线上每个位置的连接情况
//
// 时间复杂度: O(n * m * 状态数)
// 空间复杂度: O(状态数)

public class Code11_PlugDP {
    
    // 蒙德里安的梦想 - 插头DP解法
    // 题目描述: 用1×2和2×1的骨牌铺满n×m的棋盘，求方案数
    public static long mondriaanDream(int n, int m) {
        if (n < m) {
            int temp = n;
            n = m;
            m = temp;
        }
        
        // 状态表示：使用轮廓线，每个位置有3种状态：
        // 0: 无插头，1: 有插头且需要向右延伸，2: 有插头且需要向下延伸
        Map<Integer, Long> dp = new HashMap<>();
        dp.put(0, 1L);
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                Map<Integer, Long> newDp = new HashMap<>();
                
                for (Map.Entry<Integer, Long> entry : dp.entrySet()) {
                    int mask = entry.getKey();
                    long count = entry.getValue();
                    
                    // 获取当前位置的插头状态
                    int up = (mask >> (2 * j)) & 3;
                    int left = (j > 0) ? ((mask >> (2 * (j - 1))) & 3) : 0;
                    
                    if (up == 0 && left == 0) {
                        // 情况1: 放置2×1的骨牌（向下延伸）
                        int newMask = mask | (1 << (2 * j));
                        newDp.put(newMask, newDp.getOrDefault(newMask, 0L) + count);
                        
                        // 情况2: 放置1×2的骨牌（向右延伸）
                        if (j < m - 1) {
                            newMask = mask | (2 << (2 * j));
                            newDp.put(newMask, newDp.getOrDefault(newMask, 0L) + count);
                        }
                    } else if (up == 1 && left == 0) {
                        // 情况3: 向下延伸的骨牌结束
                        int newMask = mask & ~(3 << (2 * j));
                        newDp.put(newMask, newDp.getOrDefault(newMask, 0L) + count);
                    } else if (up == 2 && left == 0) {
                        // 情况4: 向右延伸的骨牌结束
                        int newMask = mask & ~(3 << (2 * j));
                        newDp.put(newMask, newDp.getOrDefault(newMask, 0L) + count);
                    }
                }
                
                dp = newDp;
            }
        }
        
        return dp.getOrDefault(0, 0L);
    }
    
    // 哈密顿回路计数 - 插头DP解法
    // 题目描述: 在网格图中计算经过所有格点恰好一次的回路数量
    public static long hamiltonianCircuit(int n, int m) {
        if (n < m) {
            int temp = n;
            n = m;
            m = temp;
        }
        
        // 使用括号表示法表示插头状态
        Map<String, Long> dp = new HashMap<>();
        dp.put("", 1L);
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                Map<String, Long> newDp = new HashMap<>();
                
                for (Map.Entry<String, Long> entry : dp.entrySet()) {
                    String state = entry.getKey();
                    long count = entry.getValue();
                    
                    // 解析当前轮廓线状态
                    // 实现括号匹配和状态转移
                    // 这里简化实现，实际需要处理括号匹配
                    if (i == 0 && j == 0) {
                        // 起点：创建新的回路
                        String newState = "()";
                        newDp.put(newState, newDp.getOrDefault(newState, 0L) + count);
                    } else if (i == n - 1 && j == m - 1) {
                        // 终点：闭合回路
                        if (state.equals("")) {
                            newDp.put("", newDp.getOrDefault("", 0L) + count);
                        }
                    } else {
                        // 中间点：处理插头连接
                        // 简化实现，实际需要复杂的状态转移
                        newDp.put(state, newDp.getOrDefault(state, 0L) + count);
                    }
                }
                
                dp = newDp;
            }
        }
        
        return dp.getOrDefault("", 0L);
    }
    
    // 网格图的最小生成树计数 - 插头DP解法
    public static long minSpanningTreeCount(int n, int m) {
        // 使用轮廓线DP计算最小生成树数量
        // 状态表示当前轮廓线上每个位置的连通分量信息
        
        Map<Integer, Long> dp = new HashMap<>();
        dp.put(0, 1L);
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                Map<Integer, Long> newDp = new HashMap<>();
                
                for (Map.Entry<Integer, Long> entry : dp.entrySet()) {
                    int mask = entry.getKey();
                    long count = entry.getValue();
                    
                    // 获取相邻位置的连通分量信息
                    int up = (i > 0) ? getComponent(mask, j) : -1;
                    int left = (j > 0) ? getComponent(mask, j - 1) : -1;
                    
                    if (up == -1 && left == -1) {
                        // 创建新的连通分量
                        int newMask = setComponent(mask, j, 1);
                        newDp.put(newMask, newDp.getOrDefault(newMask, 0L) + count);
                    } else if (up != -1 && left == -1) {
                        // 向上延伸
                        int newMask = mask;
                        newDp.put(newMask, newDp.getOrDefault(newMask, 0L) + count);
                    } else if (up == -1 && left != -1) {
                        // 向左延伸
                        int newMask = setComponent(mask, j, left);
                        newDp.put(newMask, newDp.getOrDefault(newMask, 0L) + count);
                    } else {
                        // 合并连通分量
                        if (up == left) {
                            // 形成环，不合法
                            continue;
                        }
                        int newMask = mergeComponents(mask, j, up, left);
                        newDp.put(newMask, newDp.getOrDefault(newMask, 0L) + count);
                    }
                }
                
                dp = newDp;
            }
        }
        
        return dp.getOrDefault(0, 0L);
    }
    
    // 辅助方法：获取指定位置的连通分量
    private static int getComponent(int mask, int pos) {
        return (mask >> (2 * pos)) & 3;
    }
    
    // 辅助方法：设置指定位置的连通分量
    private static int setComponent(int mask, int pos, int comp) {
        return (mask & ~(3 << (2 * pos))) | (comp << (2 * pos));
    }
    
    // 辅助方法：合并两个连通分量
    private static int mergeComponents(int mask, int pos, int comp1, int comp2) {
        // 将comp2的所有出现替换为comp1
        int result = mask;
        for (int i = 0; i < 8; i++) { // 假设最多8个位置
            if (((result >> (2 * i)) & 3) == comp2) {
                result = setComponent(result, i, comp1);
            }
        }
        return result;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试蒙德里安的梦想
        System.out.println("蒙德里安的梦想测试:");
        System.out.println("2x3网格: " + mondriaanDream(2, 3));
        System.out.println("3x3网格: " + mondriaanDream(3, 3));
        
        // 测试哈密顿回路计数
        System.out.println("\n哈密顿回路计数测试:");
        System.out.println("2x2网格: " + hamiltonianCircuit(2, 2));
        System.out.println("3x3网格: " + hamiltonianCircuit(3, 3));
        
        // 测试最小生成树计数
        System.out.println("\n最小生成树计数测试:");
        System.out.println("2x2网格: " + minSpanningTreeCount(2, 2));
        System.out.println("2x3网格: " + minSpanningTreeCount(2, 3));
    }
}

/*
 * C++ 实现
 */
// #include <iostream>
// #include <unordered_map>
// #include <string>
// using namespace std;
// 
// // 蒙德里安的梦想 - 插头DP解法
// long long mondriaanDream(int n, int m) {
//     if (n < m) swap(n, m);
//     
//     unordered_map<int, long long> dp;
//     dp[0] = 1;
//     
//     for (int i = 0; i < n; i++) {
//         for (int j = 0; j < m; j++) {
//             unordered_map<int, long long> newDp;
//             
//             for (auto& [mask, count] : dp) {
//                 int up = (mask >> (2 * j)) & 3;
//                 int left = (j > 0) ? ((mask >> (2 * (j - 1))) & 3) : 0;
//                 
//                 if (up == 0 && left == 0) {
//                     // 放置2×1的骨牌
//                     int newMask = mask | (1 << (2 * j));
//                     newDp[newMask] += count;
//                     
//                     // 放置1×2的骨牌
//                     if (j < m - 1) {
//                         newMask = mask | (2 << (2 * j));
//                         newDp[newMask] += count;
//                     }
//                 } else if (up == 1 && left == 0) {
//                     int newMask = mask & ~(3 << (2 * j));
//                     newDp[newMask] += count;
//                 } else if (up == 2 && left == 0) {
//                     int newMask = mask & ~(3 << (2 * j));
//                     newDp[newMask] += count;
//                 }
//             }
//             
//             dp = newDp;
//         }
//     }
//     
//     return dp[0];
// }
// 
// int main() {
//     cout << "蒙德里安的梦想测试:" << endl;
//     cout << "2x3网格: " << mondriaanDream(2, 3) << endl;
//     cout << "3x3网格: " << mondriaanDream(3, 3) << endl;
//     return 0;
// }

/*
 * Python 实现
 *
 * def mondriaan_dream(n, m):
 *     if n < m:
 *         n, m = m, n
 *     
 *     dp = {0: 1}
 *     
 *     for i in range(n):
 *         for j in range(m):
 *             new_dp = {}
 *             
 *             for mask, count in dp.items():
 *                 up = (mask >> (2 * j)) & 3
 *                 left = (mask >> (2 * (j - 1))) & 3 if j > 0 else 0
 *                 
 *                 if up == 0 and left == 0:
 *                     # 放置2×1的骨牌
 *                     new_mask = mask | (1 << (2 * j))
 *                     new_dp[new_mask] = new_dp.get(new_mask, 0) + count
 *                     
 *                     # 放置1×2的骨牌
 *                     if j < m - 1:
 *                         new_mask = mask | (2 << (2 * j))
 *                         new_dp[new_mask] = new_dp.get(new_mask, 0) + count
 *                 elif up == 1 and left == 0:
 *                     new_mask = mask & ~(3 << (2 * j))
 *                     new_dp[new_mask] = new_dp.get(new_mask, 0) + count
 *                 elif up == 2 and left == 0:
 *                     new_mask = mask & ~(3 << (2 * j))
 *                     new_dp[new_mask] = new_dp.get(new_mask, 0) + count
 *             
 *             dp = new_dp
 *     
 *     return dp.get(0, 0)
 * 
 * if __name__ == "__main__":
 *     print("蒙德里安的梦想测试:")
 *     print("2x3网格:", mondriaan_dream(2, 3))
 *     print("3x3网格:", mondriaan_dream(3, 3))
 */