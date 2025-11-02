#include <vector>
#include <queue>
#include <algorithm>
#include <iostream>
#include <climits>

// 组团买票
// 景区里一共有m个项目，景区的第i个项目有如下两个参数：
// game[i] = { Ki, Bi }，Ki、Bi一定是正数
// Ki代表折扣系数，Bi代表票价
// 举个例子 : Ki = 2, Bi = 10
// 如果只有1个人买票，单张门票的价格为 : Bi - Ki * 1 = 8
// 所以这1个人游玩该项目要花8元
// 如果有2个人买票，单张门票的价格为 : Bi - Ki * 2 = 6
// 所以这2个人游玩该项目要花6 * 2 = 12元
// 如果有5个人买票，单张门票的价格为 : Bi - Ki * 5 = 0
// 所以这5个人游玩该项目要花5 * 0 = 0元
// 如果有更多人买票，都认为花0元(因为让项目倒贴钱实在是太操蛋了)
// 于是可以认为，如果有x个人买票，单张门票的价格为 : Bi - Ki * x
// x个人游玩这个项目的总花费是 : max { x * (Bi - Ki * x), 0 }
// 单位一共有n个人，每个人最多可以选1个项目来游玩，也可以不选任何项目
// 所有员工将在明晚提交选择，然后由你去按照上面的规则，统一花钱购票
// 你想知道自己需要准备多少钱，就可以应付所有可能的情况，返回这个最保险的钱数
// 数据量描述 : 
// 1 <= M、N、Ki、Bi <= 10^5
// 来自真实大厂笔试，没有在线测试，对数器验证

struct Game {
    long long ki;     // 折扣系数
    long long bi;     // 门票原价
    int people;       // 之前的人数

    Game(long long k, long long b) : ki(k), bi(b), people(0) {}

    // 如果再来一人，这个项目得到多少钱
    long long earn() const {
        // bi - (people + 1) * ki : 当前的人，门票原价减少了，当前的门票价格
        // people * ki : 当前人的到来，之前的所有人，门票价格都再减去ki
        return bi - (people + 1) * ki - people * ki;
    }

    long long cost(long long p) const {
        long long price = ki * p + bi;
        if (price < 0) {
            price = 0;
        }
        return p * price;
    }
};

// 自定义比较函数，用于优先队列
struct Compare {
    bool operator()(const Game& a, const Game& b) const {
        return a.earn() < b.earn();  // 大根堆
    }
};

class Solution {
public:
    /**
     * 计算组团买票的最少花费
     * 
     * 算法思路：
     * 使用优先队列的贪心策略：
     * 1. 将所有项目加入优先队列，按收益排序（收益最大的在堆顶）
     * 2. 每次将一个人分配给当前收益最大的项目
     * 3. 更新该项目的收益并重新加入队列
     * 4. 重复直到所有人都被分配或没有正收益项目
     * 
     * 时间复杂度：O(n * logm) - n个人，m个项目，每次操作需要logm时间
     * 空间复杂度：O(m) - 优先队列存储m个项目
     * 
     * @param n 人数
     * @param games 项目数组，每个项目包含Ki和Bi两个参数
     * @return 最少花费
     */
    static long long enough2(int n, std::vector<std::vector<int>>& games) {
        // 哪个项目，再来一人挣得最多
        // 大根堆
        std::priority_queue<Game, std::vector<Game>, Compare> heap;
        
        for (const auto& g : games) {
            heap.push(Game(g[0], g[1]));
        }
        
        long long ans = 0;
        for (int i = 0; i < n; i++) {
            // 一个个的人，依次送到当前最挣钱的项目里去
            if (heap.top().earn() <= 0) {
                break;
            }
            
            Game cur = heap.top();
            heap.pop();
            
            long long money = cur.earn();
            ans += money;
            cur.people++;
            
            if (cur.earn() > 0) {
                heap.push(cur);
            }
        }
        return ans;
    }
};

// 测试用例
int main() {
    // 额外的测试用例
    std::vector<std::vector<int>> testGames = {{2, 10}, {3, 15}, {1, 8}};
    int testN = 5;
    
    std::cout << "\n额外测试用例:" << std::endl;
    std::cout << "项目参数: [[2, 10], [3, 15], [1, 8]]" << std::endl;
    std::cout << "人数: " << testN << std::endl;
    std::cout << "最少花费: " << Solution::enough2(testN, testGames) << std::endl;
    
    return 0;
}