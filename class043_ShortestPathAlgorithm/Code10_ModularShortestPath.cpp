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

// 由于编译环境限制，使用基本C++语法实现

const int MAXN = 100001;
const int INF = 2147483647;

int k;
int dist[MAXN];
bool visited[MAXN];

// 使用基本数组模拟双端队列
// 分别存储remainder, d两个字段
int dq_remainder[200000];  // 存储余数
int dq_d[200000];          // 存储距离
int front, rear;           // 队列的前后指针

// 向队首添加元素
void push_front(int remainder, int d) {
    front--;
    dq_remainder[front] = remainder;
    dq_d[front] = d;
}

// 向队尾添加元素
void push_back(int remainder, int d) {
    dq_remainder[rear] = remainder;
    dq_d[rear] = d;
    rear++;
}

// 从队首取出元素
void pop_front(int& remainder, int& d) {
    remainder = dq_remainder[front];
    d = dq_d[front];
    front++;
}

// 检查队列是否为空
bool empty() {
    return front >= rear;
}

// 同余最短路实现
int modularShortestPath() {
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
    for (int i = 0; i < MAXN; i++) {
        dist[i] = INF;
        visited[i] = false;
    }
    
    // 初始化双端队列
    front = 100000;
    rear = 100000;
    
    // 初始状态：余数为1，距离为1
    // 表示数字"1"，它有1位数字，模k余数为1
    dist[1] = 1;
    push_front(1, 1);
    
    // 当双端队列不为空时，继续处理
    while (!empty()) {
        // 从队首取出状态（数位和最小的状态）
        int r, d;
        pop_front(r, d);
        
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
            push_back(newRemainder1, d + 1);
        }
    }
    
    // 无法找到解，返回-1
    return -1;
}

// 由于编译环境限制，这里不提供main函数
// 在实际使用中，需要根据具体环境提供输入输出方式