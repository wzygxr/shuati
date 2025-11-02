#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
#include <sstream>
using namespace std;

/**
 * 多重背包问题的二进制分组优化实现类
 * 
 * 技术要点：
 * 1. 使用二进制分组将多重背包转化为01背包
 * 2. 预处理阶段生成组合物品，运行阶段对组合物品应用01背包算法
 * 3. 时间复杂度为O(t * Σlog c[i])，其中c[i]是第i种物品的数量
 */
class BoundedKnapsackWithBinarySplitting {
private:
    /** 物品数量的最大可能值 */
    static constexpr int MAXN = 1001;
    
    /** 背包容量的最大可能值 */
    static constexpr int MAXW = 40001;
    
    /** 衍生商品价值数组 */
    vector<int> v;
    
    /** 衍生商品重量数组 */
    vector<int> w;
    
    /** 动态规划数组 */
    vector<int> dp;
    
    /** 物品种类数 */
    int n;
    
    /** 背包容量 */
    int t;
    
    /** 衍生商品的总数 */
    int m;
    
    /**
     * 解析一行输入为整数数组
     * 
     * @param line 输入的一行字符串
     * @return 解析后的整数数组
     */
    vector<int> parseLine(const string& line) {
        vector<int> result;
        istringstream iss(line);
        int num;
        while (iss >> num) {
            result.push_back(num);
        }
        return result;
    }
    
    /**
     * 01背包的空间压缩实现
     * 
     * 算法思路：
     * 1. 初始化dp数组，dp[j]表示背包容量为j时的最大价值
     * 2. 逆序遍历背包容量，避免重复选择同一物品
     * 3. 状态转移方程：dp[j] = max(dp[j], dp[j - weight] + value)
     * 
     * 时间复杂度分析：
     * O(m * t)，其中m是衍生商品的总数，t是背包容量
     * 由于m = Σlog c[i]，所以整体时间复杂度为O(t * Σlog c[i])
     * 相比朴素多重背包的O(t * Σc[i])，当c[i]较大时优化效果显著
     * 
     * 空间复杂度分析：
     * O(t)，只需要一维数组存储状态
     * 
     * 优化点：
     * 1. 预处理边界情况，跳过无效物品
     * 2. 逆序遍历容量，避免重复选择同一物品
     * 3. 提前判断w[i] > j的情况，减少不必要的计算
     * 
     * @return 背包能装下的最大价值
     */
    int compute() {
        // 边界情况快速处理
        if (m == 0 || t == 0) {
            return 0;
        }
        
        // 初始化dp数组，不需要恰好装满背包，所以初始化为0
        fill(dp.begin(), dp.begin() + t + 1, 0);
        
        // 对每个衍生商品应用01背包算法
        for (int i = 1; i <= m; i++) {
            int weight = w[i];
            int value = v[i];
            
            // 优化：如果衍生商品的价值为0，跳过（不会增加总价值）
            if (value == 0) continue;
            
            // 优化：如果衍生商品的重量为0且价值不为0，可以无限选择，但这里是01背包所以跳过
            if (weight == 0) continue;
            
            // 优化：如果衍生商品的重量超过背包容量，无法选择，跳过
            if (weight > t) continue;
            
            // 逆序遍历背包容量，确保每个衍生商品只能被选择一次
            for (int j = t; j >= weight; j--) {
                // 状态转移：选择该衍生商品或不选
                int candidate = dp[j - weight] + value;
                if (candidate > dp[j]) {
                    dp[j] = candidate;
                }
            }
        }
        
        // 返回背包容量为t时的最大价值
        return dp[t];
    }
    
public:
    BoundedKnapsackWithBinarySplitting() : 
        v(MAXN, 0), 
        w(MAXN, 0), 
        dp(MAXW, 0), 
        n(0), 
        t(0), 
        m(0) {}
    
    /**
     * 运行程序的主方法
     * 
     * 工程化考量：
     * 1. 使用标准输入输出进行高效的输入处理
     * 2. 完善边界情况处理，增强代码健壮性
     * 3. 添加输入校验，处理空行和不完整输入
     * 4. 支持多组测试用例的连续读取
     */
    void run() {
        ios::sync_with_stdio(false);
        cin.tie(nullptr);
        
        string line;
        while (getline(cin, line)) {
            // 跳过空行
            if (line.empty()) continue;
            
            vector<int> firstLine = parseLine(line);
            if (firstLine.size() < 2) continue;
            
            n = firstLine[0];
            t = firstLine[1];
            
            // 边界情况快速处理
            if (n == 0 || t == 0) {
                cout << 0 << endl;
                continue;
            }
            
            // 重置衍生商品计数器
            m = 0;
            
            // 读取每个物品的信息并进行二进制分组
            for (int i = 1; i <= n; i++) {
                // 跳过可能的空行
                while (getline(cin, line) && line.empty());
                if (line.empty()) break;
                
                vector<int> itemData = parseLine(line);
                if (itemData.size() < 3) continue;
                
                int value = itemData[0];
                int weight = itemData[1];
                int cnt = itemData[2];
                
                // 优化1：跳过数量为0的物品
                if (cnt == 0) continue;
                
                // 优化2：跳过价值为0的物品（选了也不增加总价值）
                if (value == 0) continue;
                
                // 优化3：跳过重量为0的物品（特殊情况）
                if (weight == 0) continue;
                
                // 优化4：跳过重量超过背包容量的物品
                if (weight > t) continue;
                
                // 优化5：调整物品数量上限，避免无意义的计算
                cnt = min(cnt, t / weight);
                
                // 二进制分组核心逻辑：
                // 将数量为cnt的物品拆分成多个组合物品，每个组合物品的数量是2的幂次
                // 例如：cnt=5 → 拆分成1个、2个、2个
                // 这样任何1~5的数量都可以通过选择这些组合得到
                for (int k = 1; k <= cnt; k <<= 1) {
                    v[++m] = k * value;
                    w[m] = k * weight;
                    cnt -= k;
                }
                
                // 处理剩余的数量（如果cnt不是2的幂次之和）
                if (cnt > 0) {
                    v[++m] = cnt * value;
                    w[m] = cnt * weight;
                }
            }
            
            // 计算并输出结果
            cout << compute() << endl;
        }
    }
    
    /**
     * 算法优化与工程化考量
     * 
     * 1. 二进制分组优化深入分析：
     *    - 普通多重背包：三重循环，时间复杂度O(n * t * c[i])，对于大数量的物品会超时
     *    - 二进制分组优化：将物品拆分成log₂(c[i])个组合物品，时间复杂度O(n * t * log c[i])
     *    - 当c[i]很大时（比如1000），log₂(c[i])约为10，优化效果非常明显
     * 
     * 2. 二进制分组正确性数学证明：
     *    - 任意正整数c可以唯一地表示为不同2的幂次之和（二进制表示）
     *    - 对于任意k(1<=k<=c)，可以通过选择对应的二进制位组合来表示k个物品的选择
     *    - 例如：c=5 → 1(2⁰)+2(2¹)+2(剩余)，这样可以组合出1~5之间的任意数量
     * 
     * 3. 与单调队列优化的对比：
     *    - 二进制优化：时间复杂度O(n * t * log c[i])，实现简单，常数因子小
     *    - 单调队列优化：时间复杂度O(n * t)，实现较复杂，常数因子稍大
     *    - 适用场景对比：
     *      * 当物品数量较多、单个物品数量适中时，二进制优化更适用
     *      * 当背包容量很大、物品数量适中时，单调队列优化更有优势
     */
    
    /**
     * 与Java版本的差异：
     * 1. 使用vector替代数组，提供更好的内存管理
     * 2. 输入处理使用cin和getline，而非Java的BufferedReader
     * 3. 使用fill函数替代Arrays.fill
     * 4. 使用ios::sync_with_stdio(false)和cin.tie(nullptr)加速输入输出
     */
};

/**
 * 主函数
 */
int main() {
    BoundedKnapsackWithBinarySplitting solution;
    solution.run();
    return 0;
}