package class143;

// 同余最短路扩展练习题：正整数倍的最小数位和
// 给定一个正整数k，求最小的正整数n，使得n是k的倍数且n的每一位都是1。
// 例如，k=3时，n=111（因为111是3的倍数且每一位都是1）。
// 如果不存在这样的n，输出-1。
// 测试链接: https://atcoder.jp/contests/abc077/tasks/arc084_b
// 
// 算法思路：
// 这是一道典型的同余最短路问题。
// 1. 我们可以将问题转化为在模k意义下的最短路问题
// 2. 每个节点表示模k的余数
// 3. 从当前余数r可以通过两种操作转移到新余数：
//    - 添加一个1到末尾：新余数 = (r * 10 + 1) % k
//    - 这种操作的边权为1（因为添加了一个数字1）
// 4. 使用01-BFS求解从余数1到余数0的最短路径
//
// 具体实现：
// 1. 特殊情况处理：k为1时答案为1，k为2或5的倍数时无解
// 2. 使用状态(remainder, dist)表示当前余数和到达该余数的最小数位和
// 3. 通过添加数字1进行转移：(r * 10 + 1) % k
// 4. 使用双端队列存储待处理的状态
// 5. 边权为1的边添加到队尾
//
// 时间复杂度：O(k)
// 空间复杂度：O(k)
//
// 相关题目链接：
// 1. AtCoder Regular Contest 084 B - Small Multiple - https://atcoder.jp/contests/arc084/tasks/arc084_b
// 2. 洛谷 P3403 跳楼机 - https://www.luogu.com.cn/problem/P3403
// 3. 洛谷 P2371 墨墨的等式 - https://www.luogu.com.cn/problem/P2371
// 4. 洛谷 P2662 牛场围栏 - https://www.luogu.com.cn/problem/P2662
// 5. HDU 6071 Lazy Running - https://acm.hdu.edu.cn/showproblem.php?pid=6071
// 6. LeetCode 743. 网络延迟时间 - https://leetcode.cn/problems/network-delay-time/
// 7. LeetCode 542. 01 矩阵 - https://leetcode.cn/problems/01-matrix/
// 8. LeetCode 773. 滑动谜题 - https://leetcode.cn/problems/sliding-puzzle/
// 9. POJ 3403 跳楼机 - http://poj.org/problem?id=3403
// 10. POJ 2662 牛场围栏 - http://poj.org/problem?id=2662
// 11. Codeforces 241E Flights - https://codeforces.com/problemset/problem/241/E
// 12. ZOJ 3403 跳楼机 - https://zoj.pintia.cn/problem-sets/91827364500/problems/91827367903
// 13. 牛客 NC50522 跳楼机 - https://ac.nowcoder.com/acm/problem/50522
// 14. SPOJ KPEQU - https://www.spoj.com/problems/KPEQU/
// 15. 51Nod 1350 斐波那契表示 - https://www.51nod.com/Challenge/Problem.html#problemId=1350

/*
 * 算法思路：
 * 这是一道典型的同余最短路问题。
 * 1. 我们可以将问题转化为在模k意义下的最短路问题
 * 2. 每个节点表示模k的余数
 * 3. 从当前余数r可以通过两种操作转移到新余数：
 *    - 添加一个1到末尾：新余数 = (r * 10 + 1) % k
 *    - 这种操作的边权为1（因为添加了一个数字1）
 * 4. 使用01-BFS求解从余数0到余数0的最短路径（除了起点）
 * 
 * 时间复杂度：O(k)
 * 空间复杂度：O(k)
 * 
 * 示例：
 * 输入：k = 3
 * 输出：3（对应数字111）
 * 
 * 输入：k = 7
 * 输出：6（对应数字111111）
 * 
 * 输入：k = 5
 * 输出：-1（不存在这样的数字）
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.ArrayDeque;
import java.util.Arrays;

public class Code10_ModularShortestPath {

    // 最大k值
    public static int MAXN = 100001;
    
    // 正整数k
    public static int k;
    
    // 距离数组
    public static int[] dist = new int[MAXN];
    
    // 访问标记数组
    public static boolean[] visited = new boolean[MAXN];
    
    // 同余最短路实现
    public static int modularShortestPath() {
        // 特殊情况：k为1时，答案为1
        if (k == 1) {
            return 1;
        }
        
        // 特殊情况：k为2或5的倍数时，不存在解
        // 因为只有个位数是1的数字，只有在k不包含因子2和5时才可能有解
        if (k % 2 == 0 || k % 5 == 0) {
            return -1;
        }
        
        // 初始化距离数组为无穷大
        Arrays.fill(dist, Integer.MAX_VALUE);
        // 初始化访问标记数组为false
        Arrays.fill(visited, false);
        
        // 双端队列，用于存储待处理的状态
        ArrayDeque<State> deque = new ArrayDeque<>();
        
        // 初始状态：余数为1，距离为1
        // 表示数字"1"，它有1位数字，模k余数为1
        dist[1] = 1;
        deque.addFirst(new State(1, 1));
        
        // 当双端队列不为空时，继续处理
        while (!deque.isEmpty()) {
            // 从队首取出状态（数位和最小的状态）
            State curr = deque.pollFirst();
            int r = curr.remainder;
            int d = curr.dist;
            
            // 如果已经访问过，跳过（避免重复处理）
            if (visited[r]) {
                continue;
            }
            
            // 标记为已访问
            visited[r] = true;
            
            // 如果余数为0，说明找到了k的倍数，返回数位和
            if (r == 0) {
                return d;
            }
            
            // 转移操作：
            // 添加一个1到末尾：新余数 = (r * 10 + 1) % k，边权为1
            // 这表示在当前数字后面添加一个数字1，例如从"11"变为"111"
            int newRemainder1 = (r * 10 + 1) % k;
            // 如果未访问且数位和更小，则更新距离并添加到队列
            if (!visited[newRemainder1] && d + 1 < dist[newRemainder1]) {
                dist[newRemainder1] = d + 1;
                // 边权为1的节点加入队尾
                deque.addLast(new State(newRemainder1, d + 1));
            }
        }
        
        // 无法找到解，返回-1
        return -1;
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取k值
        in.nextToken();
        k = (int) in.nval;
        
        // 计算结果并输出
        out.println(modularShortestPath());
        out.flush();
        out.close();
        br.close();
    }
    
    // 状态类，表示在模k意义下的一个状态
    static class State {
        int remainder, dist;
        
        State(int remainder, int dist) {
            this.remainder = remainder;
            this.dist = dist;
        }
    }
}