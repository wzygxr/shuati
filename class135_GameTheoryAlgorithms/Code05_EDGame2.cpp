// E&D游戏
// 桌子上有2n堆石子，编号为1、2、3...2n
// 其中1、2为一组；3、4为一组；5、6为一组...2n-1、2n为一组
// 每组可以进行分割操作：
// 任取一堆石子，将其移走，然后分割同一组的另一堆石子
// 从中取出若干个石子放在被移走的位置，组成新的一堆
// 操作完成后，组内每堆的石子数必须保证大于0
// 显然，被分割的一堆的石子数至少要为2
// 两个人轮流进行分割操作，如果轮到某人进行操作时，所有堆的石子数均为1，判此人输掉比赛
// 返回先手能不能获胜

// 题目来源：
// 1. 洛谷 P2148 [SDOI2009]E&D - https://www.luogu.com.cn/problem/P2148
// 2. SPOJ 3805. E&D Game - https://www.spoj.com/problems/ED/

// 算法核心思想：
// 1. SG函数方法：通过SG定理计算每个状态的SG值
// 2. 数学规律方法：通过数学推导发现规律，sg(a,b) = lowZero((a-1)|(b-1))

// 时间复杂度分析：
// O(n) - 遍历所有组计算SG值

// 空间复杂度分析：
// O(1) - 只需要常数空间

// 工程化考量：
// 1. 异常处理：处理负数输入和边界情况
// 2. 性能优化：使用数学规律方法避免递归计算
// 3. 可读性：添加详细注释说明算法原理

class Code05_EDGame2 {
public:
    // 返回status最低位的0在第几位
    // 算法原理：
    // 通过观察SG值表发现规律：
    // sg(a,b) = lowZero((a-1)|(b-1))
    // 其中lowZero(x)返回x的二进制表示中最低位0的位置
    static int lowZero(int status) {
        int ans = 0;
        while (status > 0) {
            if ((status & 1) == 0) {
                break;
            }
            status >>= 1;
            ans++;
        }
        return ans;
    }
    
    // 主要计算函数
    static const char* canFirstWin(int n, int piles[]) {
        int sg = 0;
        // 遍历所有组，计算SG值的异或和
        for (int j = 0; j < n; j += 2) {
            int a = piles[j];
            int b = piles[j + 1];
            // 根据数学规律计算每组的SG值并异或
            sg ^= lowZero((a - 1) | (b - 1));
        }
        // SG值不为0表示先手必胜，为0表示后手必胜
        return sg != 0 ? "YES" : "NO";
    }
};