package class036;

import java.util.*;

/**
 * POJ 3278. Catch That Cow
 * 题目链接: http://poj.org/problem?id=3278
 * 题目描述: 农夫在位置N，牛在位置K。农夫每次可以移动到N-1, N+1, 或2*N。
 * 求农夫抓到牛所需的最少移动次数。
 * 
 * 核心算法思想:
 * 1. 广度优先搜索(BFS): 寻找最短路径
 * 2. 双向BFS: 从农夫和牛同时开始搜索
 * 3. 剪枝优化: 减少不必要的状态扩展
 * 
 * 时间复杂度分析:
 * - 方法1(BFS): O(K) - 最坏情况需要遍历到K位置
 * - 方法2(双向BFS): O(K^(1/2)) - 搜索空间减半
 * - 方法3(优化BFS): O(K) - 带剪枝的BFS
 * 
 * 空间复杂度分析:
 * - 所有方法: O(K) - 需要记录访问状态
 * 
 * 相关题目:
 * 1. LeetCode 127. 单词接龙 - 类似的BFS最短路径
 * 2. LeetCode 433. 最小基因变化 - 状态转换BFS
 * 3. POJ 3126. Prime Path - 素数路径BFS
 * 
 * 工程化考量:
 * 1. 边界处理: N和K的范围限制
 * 2. 性能优化: 使用数组代替HashMap
 * 3. 内存管理: 预分配足够大的数组
 */
public class POJ3278_CatchThatCow {
    
    /**
     * 方法1: 广度优先搜索(BFS) - 基础实现
     * 思路: 使用BFS寻找从N到K的最短路径
     * 时间复杂度: O(K) - 最坏情况需要遍历到K位置
     * 空间复杂度: O(K) - 队列和访问数组的空间
     */
    public static int catchCow1(int N, int K) {
        if (N >= K) {
            return N - K; // 只能向左移动
        }
        
        final int MAX = 100000;
        boolean[] visited = new boolean[MAX + 1];
        int[] dist = new int[MAX + 1];
        Queue<Integer> queue = new LinkedList<>();
        
        queue.offer(N);
        visited[N] = true;
        dist[N] = 0;
        
        while (!queue.isEmpty()) {
            int current = queue.poll();
            
            if (current == K) {
                return dist[current];
            }
            
            // 三种移动方式
            int[] nextPositions = {current - 1, current + 1, current * 2};
            
            for (int next : nextPositions) {
                if (next >= 0 && next <= MAX && !visited[next]) {
                    visited[next] = true;
                    dist[next] = dist[current] + 1;
                    queue.offer(next);
                    
                    if (next == K) {
                        return dist[next];
                    }
                }
            }
        }
        
        return -1; // 理论上不会执行到这里
    }
    
    /**
     * 方法2: 双向BFS - 优化解法
     * 思路: 从农夫位置和牛位置同时开始BFS
     * 时间复杂度: O(K^(1/2)) - 搜索空间减半
     * 空间复杂度: O(K) - 两个队列和访问数组
     */
    public static int catchCow2(int N, int K) {
        if (N >= K) {
            return N - K;
        }
        
        final int MAX = 100000;
        int[] distFromN = new int[MAX + 1];
        int[] distFromK = new int[MAX + 1];
        boolean[] visitedFromN = new boolean[MAX + 1];
        boolean[] visitedFromK = new boolean[MAX + 1];
        
        Queue<Integer> queueN = new LinkedList<>();
        Queue<Integer> queueK = new LinkedList<>();
        
        queueN.offer(N);
        visitedFromN[N] = true;
        distFromN[N] = 0;
        
        queueK.offer(K);
        visitedFromK[K] = true;
        distFromK[K] = 0;
        
        while (!queueN.isEmpty() && !queueK.isEmpty()) {
            // 从N方向扩展
            int sizeN = queueN.size();
            for (int i = 0; i < sizeN; i++) {
                int current = queueN.poll();
                
                if (visitedFromK[current]) {
                    return distFromN[current] + distFromK[current];
                }
                
                int[] nextPositions = {current - 1, current + 1, current * 2};
                for (int next : nextPositions) {
                    if (next >= 0 && next <= MAX && !visitedFromN[next]) {
                        visitedFromN[next] = true;
                        distFromN[next] = distFromN[current] + 1;
                        queueN.offer(next);
                    }
                }
            }
            
            // 从K方向扩展
            int sizeK = queueK.size();
            for (int i = 0; i < sizeK; i++) {
                int current = queueK.poll();
                
                if (visitedFromN[current]) {
                    return distFromN[current] + distFromK[current];
                }
                
                // 反向移动：只能向左移动（因为牛不动）
                if (current - 1 >= 0 && !visitedFromK[current - 1]) {
                    visitedFromK[current - 1] = true;
                    distFromK[current - 1] = distFromK[current] + 1;
                    queueK.offer(current - 1);
                }
                
                if (current + 1 <= MAX && !visitedFromK[current + 1]) {
                    visitedFromK[current + 1] = true;
                    distFromK[current + 1] = distFromK[current] + 1;
                    queueK.offer(current + 1);
                }
                
                if (current % 2 == 0 && !visitedFromK[current / 2]) {
                    visitedFromK[current / 2] = true;
                    distFromK[current / 2] = distFromK[current] + 1;
                    queueK.offer(current / 2);
                }
            }
        }
        
        return -1;
    }
    
    /**
     * 方法3: 优化BFS - 带剪枝
     * 思路: 添加剪枝条件，减少不必要的状态扩展
     * 时间复杂度: O(K) - 但常数因子更小
     * 空间复杂度: O(K) - 队列和访问数组
     */
    public static int catchCow3(int N, int K) {
        if (N >= K) {
            return N - K;
        }
        
        final int MAX = 100000;
        boolean[] visited = new boolean[MAX + 1];
        int[] dist = new int[MAX + 1];
        Queue<Integer> queue = new LinkedList<>();
        
        queue.offer(N);
        visited[N] = true;
        dist[N] = 0;
        
        while (!queue.isEmpty()) {
            int current = queue.poll();
            
            if (current == K) {
                return dist[current];
            }
            
            // 剪枝：如果当前位置已经超过K，只能向左移动
            if (current > K) {
                if (!visited[current - 1]) {
                    visited[current - 1] = true;
                    dist[current - 1] = dist[current] + 1;
                    queue.offer(current - 1);
                }
                continue;
            }
            
            // 正常三种移动方式
            int[] nextPositions = {current - 1, current + 1, current * 2};
            
            for (int next : nextPositions) {
                if (next >= 0 && next <= MAX && !visited[next]) {
                    visited[next] = true;
                    dist[next] = dist[current] + 1;
                    queue.offer(next);
                    
                    if (next == K) {
                        return dist[next];
                    }
                }
            }
        }
        
        return -1;
    }
    
    /**
     * 测试方法: 包含多种测试用例
     */
    public static void main(String[] args) {
        System.out.println("========== POJ 3278 测试 ==========");
        
        // 测试用例1: N=5, K=17
        System.out.println("测试用例1: N=5, K=17");
        System.out.println("方法1结果: " + catchCow1(5, 17));
        System.out.println("方法2结果: " + catchCow2(5, 17));
        System.out.println("方法3结果: " + catchCow3(5, 17));
        
        // 测试用例2: N=10, K=10
        System.out.println("\n测试用例2: N=10, K=10");
        System.out.println("方法1结果: " + catchCow1(10, 10));
        
        // 测试用例3: N=1, K=100000
        System.out.println("\n测试用例3: N=1, K=100000");
        System.out.println("方法1结果: " + catchCow1(1, 100000));
        
        // 性能对比说明
        System.out.println("\n========== 性能对比说明 ==========");
        System.out.println("1. 方法1（BFS）: 通用性强，逻辑清晰");
        System.out.println("2. 方法2（双向BFS）: 性能最优，适合大数据量");
        System.out.println("3. 方法3（优化BFS）: 带剪枝，实际运行更快");
    }
}

/*
Python实现:

from collections import deque

def catchCow(N, K):
    if N >= K:
        return N - K
        
    MAX = 100000
    visited = [False] * (MAX + 1)
    dist = [0] * (MAX + 1)
    queue = deque([N])
    visited[N] = True
    
    while queue:
        current = queue.popleft()
        
        if current == K:
            return dist[current]
            
        for next_pos in [current-1, current+1, current*2]:
            if 0 <= next_pos <= MAX and not visited[next_pos]:
                visited[next_pos] = True
                dist[next_pos] = dist[current] + 1
                queue.append(next_pos)
                
    return -1

C++实现:

#include <iostream>
#include <queue>
#include <vector>
using namespace std;

int catchCow(int N, int K) {
    if (N >= K) return N - K;
    
    const int MAX = 100000;
    vector<bool> visited(MAX + 1, false);
    vector<int> dist(MAX + 1, 0);
    queue<int> q;
    
    q.push(N);
    visited[N] = true;
    
    while (!q.empty()) {
        int current = q.front();
        q.pop();
        
        if (current == K) return dist[current];
        
        int nextPositions[3] = {current-1, current+1, current*2};
        for (int i = 0; i < 3; i++) {
            int next = nextPositions[i];
            if (next >= 0 && next <= MAX && !visited[next]) {
                visited[next] = true;
                dist[next] = dist[current] + 1;
                q.push(next);
            }
        }
    }
    
    return -1;
}
*/