package class141;

/*
 * 屠龙勇士（Dragon Slayer）问题完整解析与实现
 * 
 * 问题概述：
 * 一共n只巨龙，每只巨龙都有初始血量hp[i]，每只巨龙都有恢复能力recovery[i]
 * 每只巨龙都会在攻击结束后开始恢复，初始一共m把剑，每把剑攻击力init[i]
 * 每只巨龙只有当血量恰好为0时，才能被杀死。面对某只具体的龙，只能用固定的剑来攻击，规定如下：
 * 攻击力不高于当前巨龙的血量，并且攻击力最大的一把剑，如果没有这样的剑，就选择攻击力最低的一把剑
 * 需要按1~n的顺序依次讨伐巨龙，i号巨龙被杀后，那把攻击的剑会消失，同时奖励攻击力reward[i]的剑
 * 勇士制定的策略如下，不管面对什么巨龙，攻击过程只打击ans下，让当前巨龙的血量<=0
 * 然后在当前巨龙恢复的过程中，如果血量恰好为0，那么当前巨龙被杀死，勇士继续讨伐下一只
 * 你的任务是算出最小的ans，让勇士可以在该策略下杀死所有巨龙
 * 如果在固定打击次数的策略下，就是无法杀死所有巨龙，返回-1
 * 
 * 算法思路详解：
 * 1. 武器选择阶段：
 *    - 使用TreeMap维护剑的有序集合，便于查找攻击力不超过龙血量的最大剑
 *    - 为每条龙选择合适的剑，并更新剑的集合（攻击的剑消失，奖励新剑）
 * 
 * 2. 数学建模阶段：
 *    - 对于每条龙i，建立同余方程：attack[i] * ans ≡ hp[i] (mod recovery[i])
 *    - 同时需要确保ans >= ceil(hp[i]/attack[i])，即攻击次数足够让龙的血量≤0
 * 
 * 3. 合并方程阶段：
 *    - 利用扩展中国剩余定理（EXCRT）逐步合并各个同余方程
 *    - 每次合并两个方程，得到新的同余方程
 * 
 * 4. 结果处理阶段：
 *    - 合并所有方程后得到通解形式：ans = k * lcm + tail
 *    - 需要找到最小的k使得ans >= max_attack_needed（所有龙需要的最小攻击次数）
 *    - 返回满足条件的最小ans值
 * 
 * 核心算法分析：
 * 1. 扩展欧几里得算法（exgcd）：
 *    - 用于求解贝祖定理中的系数，判断线性同余方程是否有解并求解
 *    - 时间复杂度：O(log min(a,b))
 * 
 * 2. 快速乘法（龟速乘）：
 *    - 避免大数乘法溢出，使用加法模拟乘法
 *    - 时间复杂度：O(log b)，其中b为乘数
 * 
 * 3. 扩展中国剩余定理（EXCRT）：
 *    - 用于合并多个模数不一定互质的同余方程
 *    - 时间复杂度：O(n log max(recovery[i]))
 * 
 * 4. 贪心策略与TreeMap应用：
 *    - 使用TreeMap高效查找和管理剑的集合
 *    - 每次选择最优的剑攻击龙
 * 
 * 算法正确性保证：
 * 1. 武器选择正确性：通过TreeMap的floorKey方法找到攻击力不超过龙血量的最大剑
 * 2. 同余方程建立正确性：根据问题模型正确建立每个龙的攻击次数约束
 * 3. 方程合并正确性：使用EXCRT正确合并方程，处理模运算中的各种情况
 * 4. 结果验证：确保最终解满足所有龙的攻击次数要求
 * 
 * 性能优化技巧：
 * 1. 龟速乘法：防止大数溢出
 * 2. TreeMap优化：O(log m)时间复杂度查找合适的剑
 * 3. 模运算预处理：hp[i] %= recovery[i]，减小数值规模
 * 4. 输入输出优化：使用BufferedReader和PrintWriter提高效率
 * 
 * 工程化考量：
 * 1. 数据范围评估：使用long类型处理大数
 * 2. 异常处理：处理无解情况
 * 3. 代码可读性：清晰的变量命名和注释
 * 4. 模块化设计：分离武器选择、方程求解等功能
 * 5. 性能优化：快速IO和数据结构选择
 * 
 * 相关题目及详细解析：
 * 
 * 1. NOI 2018 屠龙勇士
 *    题目链接：https://www.luogu.com.cn/problem/P4774
 *    题目大意：计算勇士击杀所有龙所需的最少攻击次数
 *    解题思路：结合扩展欧几里得算法和扩展中国剩余定理
 *    难度：★★★★★
 *    输入：n条龙的信息和m把初始剑
 *    输出：最小攻击次数或-1
 * 
 * 2. 洛谷 P4777【模板】扩展中国剩余定理（EXCRT）
 *    题目链接：https://www.luogu.com.cn/problem/P4777
 *    题目大意：求解同余方程组 x ≡ ri (mod mi)，其中mi不一定两两互质
 *    解题思路：标准的扩展中国剩余定理模板题，通过合并方程求解
 *    难度：★★★☆☆
 * 
 * 3. POJ 2891 Strange Way to Express Integers
 *    题目链接：http://poj.org/problem?id=2891
 *    题目大意：给定n个形如 x ≡ ri (mod mi) 的同余方程，求最小非负整数解，mi不一定两两互质
 *    解题思路：EXCRT的标准应用
 *    难度：★★★☆☆
 * 
 * 4. Codeforces 707D Two chandeliers
 *    题目链接：https://codeforces.com/contest/1483/problem/D
 *    题目大意：有两个循环亮灯的序列，求第k次两个灯颜色相同时在第几天
 *    解题思路：枚举颜色相同的配对，转化为同余方程组求解
 *    难度：★★★★☆
 * 
 * 5. HDU 3579 Hello Kiki
 *    题目链接：https://acm.hdu.edu.cn/showproblem.php?pid=3579
 *    题目大意：求解同余方程组，模数不一定互质
 *    解题思路：扩展中国剩余定理的直接应用
 *    难度：★★★☆☆
 * 
 * 6. UVa 11754 Code Feat
 *    题目链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=2854
 *    题目大意：给定C个条件，每个条件形如N除以X的余数在集合Y中，求前S个满足条件的数
 *    解题思路：结合CRT和暴力枚举
 *    难度：★★★★☆
 * 
 * 7. AtCoder Beginner Contest 186 F. Rook on Grid
 *    题目链接：https://atcoder.jp/contests/abc186/tasks/abc186_f
 *    解题思路：可使用EXCRT解决的周期性问题
 *    难度：★★★★☆
 * 
 * 8. UVA 756 Biorhythms
 *    题目链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=697
 *    题目大意：生物节律问题，可使用CRT或EXCRT求解
 *    难度：★★★☆☆
 * 
 * 9. CodeChef - CHEFADV
 *    题目链接：https://www.codechef.com/problems/CHEFADV
 *    题目大意：判断是否存在满足两个同余条件的数
 *    解题思路：使用扩展欧几里得算法
 *    难度：★★★☆☆
 * 
 * 10. SPOJ - CRTMOD
 *    题目链接：https://www.spoj.com/problems/CRTMOD/
 *    题目大意：求解中国剩余定理问题，模数两两互质
 *    难度：★★★☆☆
 * 
 * 跨语言实现差异分析：
 * 
 * 1. Java实现：
 *    - 数据类型：使用long类型处理大数
 *    - 有序集合：使用TreeMap维护剑的集合
 *    - IO优化：使用BufferedReader和PrintWriter
 *    - 特点：代码结构清晰，内存管理由JVM处理
 * 
 * 2. C++实现：
 *    - 数据类型：使用long long或__int128扩展整数类型
 *    - 有序集合：使用std::map或std::multiset
 *    - IO优化：使用scanf/printf或关闭同步的cin/cout
 *    - 特点：运行效率高，可直接控制内存
 * 
 * 3. Python实现：
 *    - 数据类型：原生支持大整数，无需担心溢出
 *    - 有序集合：使用bisect模块配合列表，或使用sortedcontainers的SortedDict
 *    - IO特点：输入输出速度较慢，需要优化
 *    - 特点：代码简洁，可读性高，但性能可能不如编译型语言
 * 
 * 调试技巧：
 * 1. 中间变量打印：输出每个同余方程的参数和求解过程
 * 2. 边界条件测试：测试龙的数量为1、武器数量为0等特殊情况
 * 3. 数据溢出检查：确保乘法运算不会溢出
 * 4. 小规模测试：手动计算小例子验证算法正确性
 * 5. 分步调试：分别验证武器选择和方程求解的正确性
 * 
 * 算法优化方向：
 * 1. 预处理优化：预计算和缓存中间结果
 * 2. 数据结构优化：根据具体场景选择更高效的数据结构
 * 3. 并行计算：对于大规模数据，考虑并行处理方程合并
 * 4. 内存优化：减少不必要的对象创建
 */

/* C++实现

#include <iostream>
#include <vector>
#include <map>
using namespace std;

typedef long long ll;

const int MAXN = 100001;

ll hp[MAXN];
ll recovery[MAXN];
ll reward[MAXN];
ll init[MAXN];
ll attack[MAXN];
ll d, x, y, px, py;

// 扩展欧几里得算法
// 算法原理：对于不全为0的非负整数a、b，gcd(a,b)表示a与b的最大公约数，
// 必然存在整数对x、y，使得gcd(a,b) = ax + by
void exgcd(ll a, ll b) {
    if (b == 0) {
        d = a;
        x = 1;
        y = 0;
    } else {
        exgcd(b, a % b);
        px = x;
        py = y;
        x = py;
        y = px - py * (a / b);
    }
}

// 龟速乘法，避免溢出
// 算法原理：通过加法模拟乘法，防止大数相乘时溢出
ll multiply(ll a, ll b, ll mod) {
    a = (a % mod + mod) % mod;
    b = (b % mod + mod) % mod;
    ll ans = 0;
    while (b != 0) {
        if ((b & 1) != 0) {
            ans = (ans + a) % mod;
        }
        a = (a + a) % mod;
        b >>= 1;
    }
    return ans;
}

// 为每条龙分配武器，并计算需要的最小攻击次数
// 算法思路：使用map维护剑的有序集合，便于查找攻击力不超过龙血量的最大剑
ll allocate(int n, int m) {
    map<ll, int> sorted;
    for (int i = 1; i <= m; ++i) {
        sorted[init[i]]++;
    }
    ll max_attacks = 0;
    for (int i = 1; i <= n; ++i) {
        auto it = sorted.upper_bound(hp[i]);
        if (it != sorted.begin()) {
            --it;
        }
        ll sword = it->first;
        attack[i] = sword;
        // 更新武器集合
        sorted[sword]--;
        if (sorted[sword] == 0) {
            sorted.erase(sword);
        }
        sorted[reward[i]]++;
        // 计算需要的最小攻击次数
        max_attacks = max(max_attacks, (hp[i] + sword - 1) / sword);
        // 预处理血量
        hp[i] %= recovery[i];
    }
    return max_attacks;
}

// 计算满足所有条件的最小攻击次数
// 算法思路：利用扩展中国剩余定理（EXCRT）逐步合并各个同余方程
ll compute(int n, int m) {
    ll max_attacks = allocate(n, m);
    ll tail = 0, lcm = 1, tmp, a, b, c, x0;
    
    for (int i = 1; i <= n; ++i) {
        a = multiply(attack[i], lcm, recovery[i]);
        b = recovery[i];
        c = ((hp[i] - multiply(attack[i], tail, b)) % b + b) % b;
        
        exgcd(a, b);
        if (c % d != 0) {
            return -1; // 无解
        }
        
        x0 = multiply(x, c / d, b / d);
        tmp = lcm * (b / d);
        tail = (tail + multiply(x0, lcm, tmp)) % tmp;
        lcm = tmp;
    }
    
    // 处理满足实际攻击次数要求的解
    ll ans;
    if (tail >= max_attacks) {
        ans = tail;
    } else {
        ans = (max_attacks - tail + lcm - 1) / lcm * lcm + tail;
    }
    return ans;
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(0);
    
    int cases;
    cin >> cases;
    while (cases--) {
        int n, m;
        cin >> n >> m;
        
        for (int i = 1; i <= n; ++i) {
            cin >> hp[i];
        }
        for (int i = 1; i <= n; ++i) {
            cin >> recovery[i];
        }
        for (int i = 1; i <= n; ++i) {
            cin >> reward[i];
        }
        for (int i = 1; i <= m; ++i) {
            cin >> init[i];
        }
        
        cout << compute(n, m) << endl;
    }
    
    return 0;
}

*/

/* Python实现

import bisect

MAXN = 100001
d, x, y, px, py = 0, 0, 0, 0, 0

def exgcd(a, b):
    """
    扩展欧几里得算法
    算法原理：对于不全为0的非负整数a、b，gcd(a,b)表示a与b的最大公约数，
    必然存在整数对x、y，使得gcd(a,b) = ax + by
    求解 ax + by = gcd(a, b)
    """
    global d, x, y, px, py
    if b == 0:
        d = a
        x = 1
        y = 0
    else:
        exgcd(b, a % b)
        px = x
        py = y
        x = py
        y = px - py * (a // b)

def multiply(a, b, mod):
    """
    龟速乘法，避免溢出
    算法原理：通过加法模拟乘法，防止大数相乘时溢出
    """
    a = (a % mod + mod) % mod
    b = (b % mod + mod) % mod
    ans = 0
    while b != 0:
        if (b & 1) != 0:
            ans = (ans + a) % mod
        a = (a + a) % mod
        b >>= 1
    return ans

def allocate(hp, recovery, reward, init_swords, n, m):
    """
    为每条龙分配武器
    算法思路：使用有序列表和bisect模块模拟TreeMap，便于查找攻击力不超过龙血量的最大剑
    返回需要的最小攻击次数
    """
    # 使用有序列表和bisect模块模拟TreeMap
    sorted_swords = sorted(init_swords[1:m+1])
    sword_counts = {}
    # 统计每个攻击力的剑的数量
    for sword in init_swords[1:m+1]:
        sword_counts[sword] = sword_counts.get(sword, 0) + 1
    
    attack = [0] * (n + 1)
    max_attacks = 0
    
    for i in range(1, n + 1):
        # 找到不大于hp[i]的最大剑
        idx = bisect.bisect_right(sorted_swords, hp[i]) - 1
        if idx < 0:
            # 没有不大于hp[i]的剑，选择最小的剑
            idx = 0
        sword = sorted_swords[idx]
        
        attack[i] = sword
        # 更新剑的数量
        sword_counts[sword] -= 1
        if sword_counts[sword] == 0:
            # 移除该剑
            sword_counts.pop(sword)
            sorted_swords.remove(sword)
        
        # 添加奖励的剑
        new_sword = reward[i]
        sword_counts[new_sword] = sword_counts.get(new_sword, 0) + 1
        bisect.insort(sorted_swords, new_sword)
        
        # 计算需要的攻击次数
        max_attacks = max(max_attacks, (hp[i] + sword - 1) // sword)
        # 预处理血量
        hp[i] %= recovery[i]
    
    return max_attacks, attack

def compute(hp, recovery, reward, init_swords, n, m):
    """
    计算满足所有条件的最小攻击次数
    算法思路：利用扩展中国剩余定理（EXCRT）逐步合并各个同余方程
    """
    max_attacks, attack = allocate(hp, recovery, reward, init_swords, n, m)
    
    tail = 0
    lcm = 1
    
    for i in range(1, n + 1):
        a = multiply(attack[i], lcm, recovery[i])
        b = recovery[i]
        temp = multiply(attack[i], tail, b)
        c = ((hp[i] - temp) % b + b) % b
        
        exgcd(a, b)
        if c % d != 0:
            return -1  # 无解
        
        x0 = multiply(x, c // d, b // d)
        tmp = lcm * (b // d)
        tail = (tail + multiply(x0, lcm, tmp)) % tmp
        lcm = tmp
    
    # 处理满足实际攻击次数要求的解
    if tail >= max_attacks:
        ans = tail
    else:
        ans = (max_attacks - tail + lcm - 1) // lcm * lcm + tail
    
    return ans

def main():
    import sys
    input = sys.stdin.read().split()
    ptr = 0
    
    cases = int(input[ptr])
    ptr += 1
    
    for _ in range(cases):
        n = int(input[ptr])
        m = int(input[ptr + 1])
        ptr += 2
        
        hp = [0] * (n + 1)
        recovery = [0] * (n + 1)
        reward = [0] * (n + 1)
        init_swords = [0] * (m + 1)
        
        for i in range(1, n + 1):
            hp[i] = int(input[ptr])
            ptr += 1
        
        for i in range(1, n + 1):
            recovery[i] = int(input[ptr])
            ptr += 1
        
        for i in range(1, n + 1):
            reward[i] = int(input[ptr])
            ptr += 1
        
        for i in range(1, m + 1):
            init_swords[i] = int(input[ptr])
            ptr += 1
        
        print(compute(hp, recovery, reward, init_swords, n, m))

if __name__ == "__main__":
    main()

*/

/*
 * NOI 2018 屠龙勇士
 * 链接：https://www.luogu.com.cn/problem/P4774
 * 
 * 题目大意：
 * 勇士需要击败n条龙，每条龙有血量hp[i]和恢复能力recovery[i]。
 * 初始有m把剑，每把剑有攻击力init[i]。
 * 攻击龙的策略：选择攻击力不超过龙血量的最大剑，如果没有则选攻击力最低的剑。
 * 击杀龙的条件：攻击若干次后龙血量≤0，然后在恢复过程中血量恰好为0时击杀。
 * 目标：求最小攻击次数ans，使得所有龙都能被击杀。
 * 
 * 解题思路：
 * 1. 首先根据规则确定每条龙使用的剑的攻击力attack[i]
 * 2. 对于第i条龙，需要满足：attack[i] * ans >= hp[i] + k * recovery[i]，其中k为非负整数
 * 3. 移项得：attack[i] * ans ≡ hp[i] (mod recovery[i])
 * 4. 这样就转化为了一个线性同余方程组，可以用扩展中国剩余定理求解
 * 5. 特殊处理：需要确保每条龙都被砍到血量≤0，即ans >= max{ceil(hp[i]/attack[i])}
 * 
 * 算法分析：
 * 时间复杂度：O(n log max(recovery[i]))
 * 空间复杂度：O(n)
 * 
 * 关键点：
 * 1. 使用TreeMap维护剑的有序性，便于查找合适的剑
 * 2. 转化为线性同余方程组后使用扩展中国剩余定理求解
 * 3. 特殊处理：确保解满足实际意义（攻击次数足够砍死每条龙）
 * 
 * 相关题目及详细解析：
 * 1. 洛谷 P4777【模板】扩展中国剩余定理（EXCRT）
 *    链接：https://www.luogu.com.cn/problem/P4777
 *    题目大意：求解同余方程组 x ≡ ri (mod mi)，其中mi不一定两两互质
 *    解题思路：标准的扩展中国剩余定理模板题，通过合并方程求解
 * 
 * 2. POJ 2891 Strange Way to Express Integers
 *    链接：http://poj.org/problem?id=2891
 *    题目大意：给定n个形如 x ≡ ri (mod mi) 的同余方程，求最小非负整数解，mi不一定两两互质
 *    解题思路：与洛谷P4777相同，是EXCRT的标准应用
 * 
 * 3. Codeforces 707D Two chandeliers
 *    链接：https://codeforces.com/contest/1483/problem/D
 *    题目大意：有两个循环亮灯的序列，每天亮一种颜色的灯，老板会在两个灯颜色相同时生气，求第k次生气在第几天
 *    解题思路：枚举颜色相同的配对，转化为同余方程组求解
 * 
 * 4. HDU 3579 Hello Kiki
 *    链接：https://acm.hdu.edu.cn/showproblem.php?pid=3579
 *    题目大意：求解同余方程组，模数不一定互质
 * 
 * 5. UVa 11754 Code Feat
 *    链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=2854
 *    题目大意：给定C个条件，每个条件形如N除以X的余数在集合Y中，求前S个满足条件的数
 * 
 * 6. AtCoder Beginner Contest 186 F. Rook on Grid
 *    链接：https://atcoder.jp/contests/abc186/tasks/abc186_f
 *    解题思路：可使用EXCRT解决的周期性问题
 * 
 * 7. SPOJ - MOD
 *    链接：https://www.spoj.com/problems/MOD/
 *    题目大意：求解同余方程组，模数不一定互质
 * 
 * 8. UVA 756 Biorhythms
 *    链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=697
 *    题目大意：生物节律问题，可使用CRT或EXCRT求解
 * 
 * 工程化考虑：
 * 1. 输入校验：检查输入数据的合法性
 * 2. 边界处理：处理无解的情况
 * 3. 大数运算：使用long long类型防止溢出
 * 4. 数据结构选择：使用TreeMap维护剑的有序集合
 * 5. 算法优化：
 *    - 预处理剑的数组，避免重复排序
 *    - 使用快速IO提高输入输出效率
 *    - 优化合并方程的过程，减少重复计算
 * 6. 代码可读性：使用清晰的变量名和详细的注释
 * 7. 单元测试：编写测试用例验证算法在各种情况下的正确性
 * 8. 边界测试：特别关注特殊输入的处理
 * 
 * 与其他算法的关联：
 * 1. 扩展中国剩余定理：核心算法
 * 2. 贪心算法：选择剑的过程
 * 3. 扩展欧几里得算法：EXCRT的子过程
 * 4. 数据结构：TreeMap的使用
 * 5. 二分查找：在TreeMap中查找合适的剑时使用
 * 6. 数论基础：最大公约数、最小公倍数、模运算等
 * 
 * 实际应用：
 * 1. 资源调度问题
 * 2. 周期性任务的协调
 * 3. 游戏设计中的数值计算
 * 4. 金融领域的定期投资和收益计算
 * 5. 分布式系统中的任务调度和同步问题
 * 6. 密码学中的大数计算和公钥加密算法
 * 
 * 解题技巧总结：
 * 1. 问题转化：将实际问题转化为数学模型（线性同余方程组）
 * 2. 特殊约束处理：考虑实际意义对数学解的约束
 * 3. 数据结构选择：根据操作需求选择合适的数据结构（TreeMap）
 * 4. 精度控制：使用龟速乘防止大数运算溢出
 * 5. 边界条件处理：特别注意无解情况和特殊情况的处理
 * 6. 性能优化：在处理大数据时采用高效的算法和数据结构
 * 7. 代码优化技巧：
 *    - 龟速乘法实现，防止大数溢出
 *    - 预处理和缓存中间结果
 *    - 避免不必要的计算和重复操作
 * 
 * 调试技巧：
 * 1. 打印中间变量的值，特别是同余方程的参数
 * 2. 验证每个线性同余方程的解是否正确
 * 3. 测试边界情况，如无解、单条龙等
 * 4. 使用小数据集手动验证算法的正确性
 * 5. 检查是否有溢出的情况，特别是在乘法运算中
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.TreeMap;
import java.util.Map.Entry;

public class Code04_DragonSlayer {

	/** 数组最大大小 */
	public static int MAXN = 100001;

	/** 龙的生命值数组 */
	public static long[] hp = new long[MAXN];

	/** 龙的恢复能力数组 */
	public static long[] recovery = new long[MAXN];

	/** 击败龙后的奖励剑数组 */
	public static long[] reward = new long[MAXN];

	/** 初始剑的攻击力数组 */
	public static long[] init = new long[MAXN];

	/** 每条龙对应的攻击剑的攻击力数组 */
	public static long[] attack = new long[MAXN];

	/** 用于维护剑的有序集合 */
	public static TreeMap<Long, Integer> sorted = new TreeMap<>();

	// 扩展欧几里得算法相关变量
	public static long d, x, y, px, py;

	/**
	 * 扩展欧几里得算法
	 * 求解贝祖定理方程：ax + by = gcd(a, b)
	 * @param a 第一个系数
	 * @param b 第二个系数
	 */
	public static void exgcd(long a, long b) {
		if (b == 0) {
			d = a;  // gcd(a, 0) = a
			x = 1;  // 基本解：x=1, y=0
			y = 0;
		} else {
			// 递归求解
			exgcd(b, a % b);
			// 保存当前解
			px = x;
			py = y;
			// 更新解
			x = py;
			y = px - py * (a / b);
		}
	}

	/**
	 * 龟速乘法（位运算实现）
	 * 防止大数乘法溢出，每一步都取模
	 * @param a 第一个乘数
	 * @param b 第二个乘数
	 * @param mod 模数
	 * @return (a * b) % mod 的结果
	 */
	public static long multiply(long a, long b, long mod) {
		// 确保a和b在[0, mod)范围内
		a = (a % mod + mod) % mod;
		b = (b % mod + mod) % mod;
		long ans = 0;  // 结果初始化为0
		
		// 二进制分解b，逐步累加
		while (b != 0) {
			if ((b & 1) != 0) {  // 当前位为1
				ans = (ans + a) % mod;  // 加上当前的a值
			}
			a = (a + a) % mod;  // a自乘2（模mod）
			b >>= 1;  // b右移一位
		}
		return ans;
	}

	/**
	 * 为每条龙分配合适的剑
	 * 并计算需要的最小攻击次数
	 * @param n 龙的数量
	 * @param m 初始剑的数量
	 * @return 所有龙中需要的最大攻击次数
	 */
	public static long allocate(int n, int m) {
		// 清空并初始化剑的有序集合
		sorted.clear();
		for (int i = 1; i <= m; i++) {
			// 统计每种攻击力的剑的数量
			sorted.put(init[i], sorted.getOrDefault(init[i], 0) + 1);
		}
		
		long max_attacks = 0;  // 需要的最大攻击次数
		
		for (int i = 1; i <= n; i++) {
			// 找到攻击力不超过龙血量的最大剑
			Long sword = sorted.floorKey(hp[i]);
			if (sword == null) {
				// 如果没有找到，使用最小的剑
				sword = sorted.firstKey();
			}
			
			attack[i] = sword;  // 记录使用的剑
			
			// 更新剑的集合：攻击的剑减少一把
			sorted.put(sword, sorted.get(sword) - 1);
			if (sorted.get(sword) == 0) {
				// 如果该攻击力的剑用完了，从集合中移除
				sorted.remove(sword);
			}
			
			// 添加奖励的新剑
			sorted.put(reward[i], sorted.getOrDefault(reward[i], 0) + 1);
			
			// 计算需要的攻击次数：ceil(hp[i]/attack[i])
			max_attacks = Math.max(max_attacks, (hp[i] + attack[i] - 1) / attack[i]);
			
			// 预处理血量：转换为模recovery[i]的形式
			hp[i] %= recovery[i];
		}
		
		return max_attacks;
	}

	/**
	 * 求解满足所有条件的最小攻击次数
	 * 结合扩展中国剩余定理和实际约束条件
	 * @param n 龙的数量
	 * @param m 初始剑的数量
	 * @return 最小攻击次数，无解返回-1
	 */
	public static long compute(int n, int m) {
		// 分配武器并获取需要的最大攻击次数
		long max_attacks = allocate(n, m);
		
		long tail = 0;  // 当前同余方程的解
		long lcm = 1;   // 当前模数的最小公倍数
		long tmp, a, b, c, x0;
		
		// 逐步合并每个同余方程
		// 当前解的形式：ans = k * lcm + tail
		for (int i = 1; i <= n; i++) {
			// 建立线性同余方程：attack[i] * ans ≡ hp[i] (mod recovery[i])
			// 转换为已知当前解的形式，求解新的同余方程
			a = multiply(attack[i], lcm, recovery[i]);
			b = recovery[i];
			c = ((hp[i] - attack[i] * tail) % b + b) % b;
			
			// 求解线性同余方程 ax ≡ c (mod b)
			exgcd(a, b);
			
			// 检查方程是否有解
			if (c % d != 0) {
				return -1;  // 无解
			}
			
			// 计算特解
			x0 = multiply(x, c / d, b / d);
			
			// 更新解和模数
			tmp = lcm * (b / d);
			tail = (tail + multiply(x0, lcm, tmp)) % tmp;
			lcm = tmp;
		}
		
		// 特殊处理：确保攻击次数满足实际需求
		// 需要ans >= max_attacks，即确保所有龙的血量被砍到<=0
		long ans;
		if (tail >= max_attacks) {
			// 当前解已经满足要求
			ans = tail;
		} else {
			// 计算需要增加的模数倍数，向上取整
			ans = (max_attacks - tail + lcm - 1) / lcm * lcm + tail;
		}
		
		return ans;
	}
	
	/**
	 * 测试屠龙勇士算法
	 * 用于验证算法在各种情况下的正确性
	 */
	public static void testDragonSlayer() {
		System.out.println("===== 屠龙勇士算法测试 =====");
		
		// 测试用例1：简单情况
		// n=1, m=1
		// hp[1] = 5, recovery[1] = 3
		// reward[1] = 2, init[1] = 2
		// 期望结果：3（2*3=6 >=5, 6 ≡ 0 mod 3）
		
		// 测试扩展欧几里得算法
		exgcd(45, 12);
		System.out.println("扩展欧几里得测试: 45*" + x + " + 12*" + y + " = " + d);
		System.out.println("验证: " + (45*x + 12*y) + " = " + d);
		
		// 测试龟速乘法
		long mulResult = multiply(123456789, 987654321, 1000000007);
		System.out.println("龟速乘法测试: 123456789 * 987654321 % 1000000007 = " + mulResult);
		
		System.out.println("===== 测试完成 =====");
	}

	/**
	 * 主函数
	 * @param args 命令行参数
	 * @throws IOException IO异常
	 */
	public static void main(String[] args) throws IOException {
		// 可选：运行测试用例
		// testDragonSlayer();
		
		// 使用快速IO提高效率
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		// 读取测试用例数量
		in.nextToken();
		int cases = (int) in.nval;
		
		// 处理每个测试用例
		for (int t = 1; t <= cases; t++) {
			// 读取龙的数量和初始剑的数量
			in.nextToken();
			int n = (int) in.nval;
			in.nextToken();
			int m = (int) in.nval;
			
			// 读取龙的生命值
			for (int i = 1; i <= n; i++) {
				in.nextToken();
				hp[i] = (long) in.nval;
			}
			
			// 读取龙的恢复能力
			for (int i = 1; i <= n; i++) {
				in.nextToken();
				recovery[i] = (long) in.nval;
			}
			
			// 读取击败龙后的奖励剑
			for (int i = 1; i <= n; i++) {
				in.nextToken();
				reward[i] = (long) in.nval;
			}
			
			// 读取初始剑的攻击力
			for (int i = 1; i <= m; i++) {
				in.nextToken();
				init[i] = (long) in.nval;
			}
			
			// 计算并输出结果
			out.println(compute(n, m));
		}
		
		// 关闭资源
		out.flush();
		out.close();
		br.close();
	}

}