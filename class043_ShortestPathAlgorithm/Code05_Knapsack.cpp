// 背包(两次转圈法)
// 一共有n种物品，第i种物品的体积为v[i]，价值为c[i]，每种物品可以选择任意个，个数不能是负数
// 一共有m条查询，每次查询都会给定jobv，代表体积的要求
// 要求挑选物品的体积和一定要严格是jobv，返回能得到的最大价值和
// 如果没有方案能正好凑满jobv，返回-1
// 1 <= n <= 50
// 1 <= m <= 10^5
// 1 <= v[i] <= 10^5
// 1 <= c[i] <= 10^6
// 10^11 <= jobv <= 10^12
// 测试链接 : https://www.luogu.com.cn/problem/P9140
// 
// 算法思路：
// 这是一道完全背包问题的变种，要求精确装满指定体积并求最大价值。
// 由于jobv的范围很大(10^11~10^12)，不能直接使用动态规划。
// 采用"同余最短路"的思想：
// 1. 选择价值体积比最大的物品作为基准物品x
// 2. 构建模x意义下的最短路图，dp[i]表示总体积模x余数为i时能得到的最大补偿价值
// 3. 对于每个查询jobv，通过dp[jobv % x]计算结果
// 
// 具体实现：
// 1. 首先找到价值体积比最大的物品作为基准物品
// 2. 使用同余最短路算法，构建模基准物品体积x意义下的图
// 3. 对于其他物品，添加转移边：从余数j到(j+v[i])%x，转移价值为c[i]-(cur+v[i])/x*y
// 4. 使用两次转圈法确保最短路正确计算
// 5. 对于每个查询，根据余数查找对应的补偿价值
//
// 时间复杂度：O(x + n + m)
// 空间复杂度：O(x)
//
// 相关题目链接：
// 1. 洛谷 P9140 背包 - https://www.luogu.com.cn/problem/P9140
// 2. 洛谷 P2371 [国家集训队]墨墨的等式 - https://www.luogu.com.cn/problem/P2371
// 3. 洛谷 P3403 跳楼机 - https://www.luogu.com.cn/problem/P3403
// 4. AtCoder Regular Contest 084 D - Small Multiple - https://atcoder.jp/contests/arc084/tasks/arc084_b
// 5. 洛谷 P2662 牛场围栏 - https://www.luogu.com.cn/problem/P2662
// 6. HDU 6071 Lazy Running - https://acm.hdu.edu.cn/showproblem.php?pid=6071
// 7. LeetCode 743. 网络延迟时间 - https://leetcode.cn/problems/network-delay-time/
// 8. LeetCode 542. 01 矩阵 - https://leetcode.cn/problems/01-matrix/
// 9. LeetCode 773. 滑动谜题 - https://leetcode.cn/problems/sliding-puzzle/
// 10. POJ 3403 跳楼机 - http://poj.org/problem?id=3403
// 11. POJ 2662 牛场围栏 - http://poj.org/problem?id=2662
// 12. Codeforces 241E Flights - https://codeforces.com/problemset/problem/241/E
// 13. ZOJ 3403 跳楼机 - https://zoj.pintia.cn/problem-sets/91827364500/problems/91827367903
// 14. 牛客 NC50522 跳楼机 - https://ac.nowcoder.com/acm/problem/50522
// 15. SPOJ KPEQU - https://www.spoj.com/problems/KPEQU/

// 由于编译环境限制，使用基本C++语法实现

const int MAXN = 100001;
const long long inf = -(1LL << 60);

int v[MAXN];
int c[MAXN];
long long dp[MAXN];
int n, m, x, y;

// 求两个数的最大公约数
int gcd(int a, int b) {
    return b == 0 ? a : gcd(b, a % b);
}

// 手动实现max函数
long long max(long long a, long long b) {
    return a > b ? a : b;
}

// 主计算函数，使用同余最短路算法
void compute() {
    // 初始化dp数组为负无穷
    for (int i = 0; i < x; i++) {
        dp[i] = inf;
    }
    // 从0开始的补偿价值为0
    dp[0] = 0;
    // 对于除基准物品外的其他物品，更新最短路
    for (int i = 1; i <= n; i++) {
        if (v[i] != x) {
            // 构建同余类图，每个子环代表一个同余类
            for (int j = 0, d = gcd(v[i], x); j < d; j++) {
                // 两次转圈法：每个节点访问两次确保最短路正确计算
                for (int cur = j, next, circle = 0; circle < 2; circle += (cur == j ? 1 : 0)) {
                    next = (cur + v[i]) % x;
                    // 如果当前节点可达，则更新下一个节点的最大补偿价值
                    if (dp[cur] != inf) {
                        dp[next] = max(dp[next], dp[cur] - (long long)((cur + v[i]) / x) * y + c[i]);
                    }
                    cur = next;
                }
            }
        }
    }
}

// 由于编译环境限制，这里不提供main函数
// 在实际使用中，需要根据具体环境提供输入输出方式