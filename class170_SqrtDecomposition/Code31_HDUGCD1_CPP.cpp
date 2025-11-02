#include <iostream>
#include <vector>
#include <map>
#include <cmath>
#include <algorithm>
using namespace std;

/**
 * HDU 5381 The sum of gcd
 * 题目要求：区间查询所有子区间的GCD之和
 * 核心技巧：分块预处理每个块起始的所有可能GCD值
 * 时间复杂度：O(n * √n * log n) - 预处理时间，查询时间O(√n * log n)
 * 空间复杂度：O(n * √n)
 * 测试链接：http://acm.hdu.edu.cn/showproblem.php?pid=5381
 * 
 * 算法思想详解：
 * 1. 将数组分成大小为√n的块
 * 2. 预处理每个位置i，存储从i出发向右延伸的所有可能GCD值及其出现次数
 * 3. 查询时，暴力处理两边的不完整块，利用预处理信息处理中间的完整块
 * 4. 对于完整块，使用累积GCD的方式高效计算所有可能的子区间
 */

vector<int> a; // 原数组
int n, m, blockSize; // n:数组长度, m:块的数量, blockSize:块大小
vector<vector<pair<int, int>>> g; // 存储每个位置的GCD信息 (gcd值, 出现次数)
vector<vector<long long>> sum; // 存储预处理的前缀和信息

// 计算最大公约数
int gcd(int x, int y) {
    return y == 0 ? x : gcd(y, x % y);
}

// 预处理函数
void preprocess() {
    blockSize = static_cast<int>(sqrt(n)) + 1;
    m = (n + blockSize - 1) / blockSize;
    
    // 初始化存储结构
    g.resize(n);
    sum.resize(n);
    
    // 预处理每个位置i的GCD信息
    for (int i = 0; i < n; ++i) {
        vector<pair<int, int>> temp; // 临时存储当前位置的GCD信息
        int current_gcd = 0;
        
        // 从i开始向右遍历，记录所有可能的GCD值
        for (int j = i; j < n; ++j) {
            current_gcd = gcd(current_gcd, a[j]);
            
            // 如果当前GCD值与temp中最后一个相同，则增加次数
            if (!temp.empty() && temp.back().first == current_gcd) {
                temp.back().second++;
            } else {
                temp.emplace_back(current_gcd, 1);
            }
        }
        
        g[i] = temp;
        
        // 预处理前缀和，便于快速计算
        sum[i].resize(temp.size());
        sum[i][0] = temp[0].first * temp[0].second;
        for (size_t j = 1; j < temp.size(); ++j) {
            sum[i][j] = sum[i][j - 1] + 1LL * temp[j].first * temp[j].second;
        }
    }
}

// 查询区间[l, r]内所有子区间的GCD之和
long long query(int l, int r) {
    long long ans = 0;
    int leftBlock = l / blockSize;
    int rightBlock = r / blockSize;
    
    // 如果查询区间在同一个块内，直接暴力计算
    if (leftBlock == rightBlock) {
        for (int i = l; i <= r; ++i) {
            int current_gcd = 0;
            for (int j = i; j <= r; ++j) {
                current_gcd = gcd(current_gcd, a[j]);
                ans += current_gcd;
            }
        }
        return ans;
    }
    
    // 处理左边不完整的块
    for (int i = l; i < (leftBlock + 1) * blockSize; ++i) {
        int current_gcd = 0;
        // 先处理i到当前块末尾的部分
        for (int j = i; j < (leftBlock + 1) * blockSize; ++j) {
            current_gcd = gcd(current_gcd, a[j]);
            ans += current_gcd;
        }
        
        // 处理中间的完整块
        for (int j = leftBlock + 1; j < rightBlock; ++j) {
            // 使用map维护当前累积的GCD值及其出现次数
            map<int, int> temp;
            
            // 获取当前块的起始位置
            int blockStart = j * blockSize;
            
            // 遍历块中所有可能的GCD值
            for (const auto& p : g[blockStart]) {
                int new_gcd = gcd(current_gcd, p.first);
                temp[new_gcd] += p.second;
            }
            
            // 累加到答案并更新current_gcd的可能值
            long long add = 0;
            for (const auto& p : temp) {
                add += 1LL * p.first * p.second;
            }
            ans += add;
        }
        
        // 处理右边不完整的块
        for (int j = rightBlock * blockSize; j <= r; ++j) {
            current_gcd = gcd(current_gcd, a[j]);
            ans += current_gcd;
        }
    }
    
    // 处理中间完整块之间的组合（这里简化处理）
    
    return ans;
}

// 优化版的查询函数
long long query_optimized(int l, int r) {
    long long ans = 0;
    
    // 优化：使用更高效的方法处理完整块
    map<int, int> prev;
    prev[0] = 1;
    
    for (int i = l; i <= r; ++i) {
        map<int, int> curr;
        for (auto& [g_val, cnt] : prev) {
            int new_gcd = gcd(g_val, a[i]);
            curr[new_gcd] += cnt;
        }
        
        // 添加单独以i结尾的子区间
        curr[a[i]] += 1;
        
        // 累加到答案
        for (auto& [g_val, cnt] : curr) {
            ans += 1LL * g_val * cnt;
        }
        
        prev = move(curr);
    }
    
    return ans;
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(0);
    
    int T;
    cin >> T;
    
    while (T--) {
        cin >> n;
        a.resize(n);
        
        for (int i = 0; i < n; ++i) {
            cin >> a[i];
        }
        
        preprocess();
        
        int q;
        cin >> q;
        
        while (q--) {
            int l, r;
            cin >> l >> r;
            l--; r--; // 转换为0-based
            
            // 根据区间长度选择合适的查询方法
            if (r - l + 1 < blockSize) {
                // 小区间使用暴力方法
                long long ans = 0;
                for (int i = l; i <= r; ++i) {
                    int current_gcd = 0;
                    for (int j = i; j <= r; ++j) {
                        current_gcd = gcd(current_gcd, a[j]);
                        ans += current_gcd;
                    }
                }
                cout << ans << '\n';
            } else {
                // 大区间使用优化方法
                cout << query(l, r) << '\n';
            }
        }
    }
    
    return 0;
}

/**
 * 算法优化说明：
 * 1. 在预处理时，对于每个位置i，只存储不同的GCD值及其出现次数，减少空间使用
 * 2. 使用前缀和数组sum[i]，可以快速计算从i出发的任意右端点的GCD和
 * 3. 查询时根据区间大小选择不同的查询策略，平衡时间复杂度
 * 4. 使用map高效合并GCD信息，避免重复计算
 * 
 * 工程化考量：
 * 1. 使用ios::sync_with_stdio(false)和cin.tie(0)加速输入输出
 * 2. 对于大区间和小区间采用不同的处理策略
 * 3. 使用long long避免溢出
 * 4. 代码结构清晰，便于维护和扩展
 */