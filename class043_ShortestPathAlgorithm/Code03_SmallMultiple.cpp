// 正整数倍的最小数位和
// 给定一个整数k，求一个k的正整数倍s，使得在十进制下，s的数位累加和最小
// 2 <= k <= 10^5
// 测试链接 : https://www.luogu.com.cn/problem/AT_arc084_b
// 测试链接 : https://atcoder.jp/contests/abc077/tasks/arc084_b

/*
 * 算法思路：
 * 这道题使用01-BFS算法解决。
 * 我们将问题建模为在模k意义下的图上找最短路径。
 * 每个节点i表示当前数字模k的余数为i，边权表示新增数位的值。
 * 有两种操作：
 * 1. 乘以10（相当于在末尾添加0），边权为0
 * 2. 加1（相当于在末尾数位加1），边权为1
 * 从节点1开始搜索，找到到达节点0的最短路径。
 * 
 * 时间复杂度：O(k)
 * 空间复杂度：O(k)
 * 
 * 题目来源：
 * 1. AtCoder ARC084_B - Small Multiple (https://atcoder.jp/contests/abc077/tasks/arc084_b)
 * 2. AtCoder ABC077_C - Snuke the Wizard
 * 
 * 相关题目：
 * 1. Codeforces 176D Wizard in Maze - 迷宫最短路 (https://codeforces.com/problemset/problem/176/D)
 * 2. UVA 11573 Ocean Currents - 海流方向移动 (https://vjudge.net/problem/UVA-11573)
 * 3. SPOJ KATHTHI - 01-BFS模板题 (https://www.spoj.com/problems/KATHTHI/)
 * 4. AtCoder ABC176_D Wizard in Maze (https://atcoder.jp/contests/abc176/tasks/abc176_d)
 * 5. LeetCode 1368 Minimum Cost to Make at Least One Valid Path in a Grid (https://leetcode.cn/problems/minimum-cost-to-make-at-least-one-valid-path-in-a-grid/)
 * 6. Codeforces 590C Three States (https://codeforces.com/contest/590/problem/C)
 * 7. LeetCode 2290 Minimum Obstacle Removal to Reach Corner (https://leetcode.cn/problems/minimum-obstacle-removal-to-reach-corner/)
 * 8. LeetCode 1824 Minimum Sideway Jumps (https://leetcode.cn/problems/minimum-sideway-jumps/)
 * 9. LeetCode 773 Sliding Puzzle (https://leetcode.cn/problems/sliding-puzzle/)
 * 10. POJ 3259 Wormholes (http://poj.org/problem?id=3259)
 * 11. HDU 6214 Smallest Minimum Cut (http://acm.hdu.edu.cn/showproblem.php?pid=6214)
 * 12. 洛谷P1429 平面最近点对 (https://www.luogu.com.cn/problem/P1429)
 * 13. 洛谷P2296 寻找道路 (https://www.luogu.com.cn/problem/P2296)
 * 14. 洛谷P2384 道路和航线 (https://www.luogu.com.cn/problem/P2384)
 * 15. 洛谷P2491 逃离僵尸岛 (https://www.luogu.com.cn/problem/P2491)
 */

// 由于编译环境问题，使用基本C++实现，避免使用STL容器

const int MAXK = 100001;

int k;

// 简化版双端队列实现
int deque_val[MAXK * 2][2];  // 存储状态 (余数, 成本)
int front_idx = 0;
int back_idx = 0;

// 访问标记数组
bool visited[MAXK];

// 初始化双端队列
void deque_init() {
    front_idx = MAXK;
    back_idx = MAXK;
}

// 从队首添加元素 (边权为0的操作)
void deque_push_front(int mod, int cost) {
    front_idx--;
    deque_val[front_idx][0] = mod;
    deque_val[front_idx][1] = cost;
}

// 从队尾添加元素 (边权为1的操作)
void deque_push_back(int mod, int cost) {
    deque_val[back_idx][0] = mod;
    deque_val[back_idx][1] = cost;
    back_idx++;
}

// 从队首取出元素
bool deque_pop_front(int& mod, int& cost) {
    if (front_idx >= back_idx) {
        return false;  // 队列为空
    }
    
    mod = deque_val[front_idx][0];
    cost = deque_val[front_idx][1];
    front_idx++;
    return true;
}

// 检查队列是否为空
bool deque_empty() {
    return front_idx >= back_idx;
}

// 01-BFS算法
int bfs() {
    deque_init();
    for (int i = 0; i < k; i++) {
        visited[i] = false;
    }
    
    // 初始状态：余数为1，数位和为1
    deque_push_front(1, 1);
    
    int mod, cost;
    while (!deque_empty()) {
        if (!deque_pop_front(mod, cost)) {
            break;
        }
        
        if (!visited[mod]) {
            visited[mod] = true;
            
            if (mod == 0) {
                return cost;
            }
            
            // 两种转移方式：
            // 1. 乘以10（在末尾加0），数位和不变，边权为0
            deque_push_front((mod * 10) % k, cost);
            // 2. 加1（末尾数位加1），数位和加1，边权为1
            deque_push_back((mod + 1) % k, cost + 1);
        }
    }
    
    return -1;
}

// 由于无法使用标准输入输出，提供一个示例函数框架
// 实际使用时需要根据具体环境实现输入输出
int solve(int k_val) {
    k = k_val;
    return bfs();
}