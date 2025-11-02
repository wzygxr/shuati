// POJ 2352 Stars
// 题目描述：给定N个星星的坐标(x,y)，满足y坐标升序排列，若y相同则x升序排列。
// 每个星星的等级是它左下角区域内星星的数量（即x坐标≤其x，y坐标≤其y的星星数目，不包括自身）。
// 输出等级为0到N-1的星星数目。
// 题目链接：http://poj.org/problem?id=2352
// 解题思路：树状数组 + 离散化

#include <iostream>
#include <vector>
#include <algorithm>
#include <unordered_map>
#include <chrono>
using namespace std;
using namespace std::chrono;

/**
 * 使用树状数组解决Stars问题
 * 
 * 时间复杂度：O(N log N)
 * 空间复杂度：O(max_x)
 * 
 * 本题特点：
 * 1. 由于输入是按y升序排列的，所以对于每个星星来说，之前处理过的星星的y坐标都不超过它的y坐标
 * 2. 因此我们只需要统计之前处理过的星星中x坐标小于等于当前星星x坐标的数量
 * 3. 这可以通过树状数组高效实现，每次查询前缀和，然后更新树状数组
 */
class Code16_Stars {
private:
    int maxX;        // 最大x坐标值
    vector<int> bit; // 树状数组
    vector<int> result; // 存储每个等级的星星数目

    /**
     * lowbit操作，获取x二进制表示中最低位的1所对应的值
     * @param x 输入整数
     * @return 最低位的1对应的值
     */
    int lowbit(int x) {
        return x & (-x);
    }

    /**
     * 更新树状数组
     * @param x 要更新的位置（1-based）
     * @param val 要增加的值
     */
    void update(int x, int val) {
        while (x <= maxX) {
            bit[x] += val;
            x += lowbit(x);
        }
    }

    /**
     * 查询前缀和，即1到x的累加和
     * @param x 查询上限（1-based）
     * @return 前缀和
     */
    int query(int x) {
        int sum = 0;
        while (x > 0) {
            sum += bit[x];
            x -= lowbit(x);
        }
        return sum;
    }

public:
    /**
     * 构造函数
     * @param max_x_value 最大x坐标值
     */
    Code16_Stars(int max_x_value) {
        // 初始化树状数组和结果数组
        // 注意：这里max_x_value+1是因为树状数组下标从1开始
        this->maxX = max_x_value;
        this->bit.resize(max_x_value + 2, 0); // +2 防止溢出
        this->result.resize(max_x_value + 1, 0); // 等级最多为max_x
    }

    /**
     * 处理星星数据，计算每个星星的等级
     * @param stars 星星坐标数组
     * @return 等级统计结果，result[i]表示等级为i的星星数目
     */
    vector<int> processStars(const vector<pair<int, int>>& stars) {
        // 统计每个星星的等级
        for (const auto& star : stars) {
            int x = star.first;
            // 由于树状数组索引从1开始，我们将x坐标+1
            // 计算当前星星的等级：查询小于等于x的星星数量
            int level = query(x + 1); // 转换为1-based索引

            // 更新等级统计
            result[level]++;

            // 将当前星星加入树状数组
            update(x + 1, 1); // 转换为1-based索引
        }

        return result;
    }

    /**
     * 处理星星数据（带离散化）
     * 当x坐标范围很大时使用离散化可以节省空间
     * @param stars 星星坐标数组
     * @return 等级统计结果
     */
    vector<int> processStarsWithDiscretization(const vector<pair<int, int>>& stars) {
        // 提取所有x坐标用于离散化
        vector<int> xs;
        xs.reserve(stars.size());
        for (const auto& star : stars) {
            xs.push_back(star.first);
        }

        // 离散化处理
        unordered_map<int, int> coordinateMapping = discretize(xs);

        // 重置树状数组为离散化后的大小
        this->maxX = coordinateMapping.size();
        this->bit.assign(this->maxX + 2, 0); // +2 防止溢出
        this->result.assign(stars.size(), 0); // 重置结果数组

        // 处理星星数据
        for (const auto& star : stars) {
            int x = star.first;
            // 获取离散化后的值（从1开始）
            int discretizedX = coordinateMapping[x] + 1;

            // 计算当前星星的等级
            int level = query(discretizedX);

            // 更新等级统计
            result[level]++;

            // 将当前星星加入树状数组
            update(discretizedX, 1);
        }

        return result;
    }

    /**
     * 离散化处理
     * @param nums 原始数据数组
     * @return 原始值到离散化值的映射
     */
    unordered_map<int, int> discretize(const vector<int>& nums) {
        // 复制并去重
        vector<int> uniqueNums(nums.begin(), nums.end());
        sort(uniqueNums.begin(), uniqueNums.end());
        uniqueNums.erase(unique(uniqueNums.begin(), uniqueNums.end()), uniqueNums.end());

        // 构建映射
        unordered_map<int, int> mapping;
        for (int i = 0; i < uniqueNums.size(); i++) {
            mapping[uniqueNums[i]] = i; // 从0开始的离散化值
        }

        return mapping;
    }

    /**
     * 打印结果
     * @param result 等级统计结果
     * @param n 星星数量
     */
    static void printResult(const vector<int>& result, int n) {
        for (int i = 0; i < n; i++) {
            cout << result[i] << endl;
        }
    }
};

/**
 * 测试函数
 */
void test() {
    cout << "=== 测试用例1（无需离散化）===" << endl;
    // 测试用例1：简单示例
    vector<pair<int, int>> stars1 = {
        {1, 1},
        {2, 2},
        {3, 3},
        {1, 3},
        {2, 1}
    };

    // 找出最大的x坐标
    int maxX1 = 0;
    for (const auto& star : stars1) {
        maxX1 = max(maxX1, star.first);
    }

    Code16_Stars solver1(maxX1);
    vector<int> result1 = solver1.processStars(stars1);
    Code16_Stars::printResult(result1, stars1.size());

    cout << "\n=== 测试用例2（使用离散化）===" << endl;
    // 测试用例2：使用离散化
    vector<pair<int, int>> stars2 = {
        {10000, 1},
        {20000, 2},
        {5000, 3},
        {10000, 3},
        {20000, 1}
    };

    Code16_Stars solver2(20000); // 初始值不重要，会在离散化时重置
    vector<int> result2 = solver2.processStarsWithDiscretization(stars2);
    Code16_Stars::printResult(result2, stars2.size());

    cout << "\n=== 测试用例3（所有星星在同一点）===" << endl;
    // 测试用例3：边界情况 - 所有星星在同一点
    vector<pair<int, int>> stars3 = {
        {1, 1},
        {1, 1},
        {1, 1}
    };

    int maxX3 = 1;
    Code16_Stars solver3(maxX3);
    vector<int> result3 = solver3.processStars(stars3);
    Code16_Stars::printResult(result3, stars3.size());
}

/**
 * 性能测试
 */
void performanceTest() {
    cout << "\n=== 性能测试 ===" << endl;
    
    // 生成测试数据
    int n = 100000;
    vector<pair<int, int>> stars;
    stars.reserve(n);
    
    // 生成随机坐标，保持y升序排列
    for (int i = 0; i < n; i++) {
        int y = i / 100; // 保证y升序
        int x = rand() % 1000000 + 1; // x坐标在1到1e6之间
        stars.emplace_back(x, y);
    }
    
    // 确保数据按y升序排列，相同y时按x升序排列
    sort(stars.begin(), stars.end(), [](const pair<int, int>& a, const pair<int, int>& b) {
        if (a.second == b.second) {
            return a.first < b.first;
        }
        return a.second < b.second;
    });
    
    // 测试普通方法
    cout << "测试处理" << stars.size() << "个星星的数据..." << endl;
    auto start = high_resolution_clock::now();
    
    // 找出最大的x坐标
    int maxX = 0;
    for (const auto& star : stars) {
        maxX = max(maxX, star.first);
    }
    
    Code16_Stars solver(maxX);
    vector<int> result = solver.processStars(stars);
    
    auto end = high_resolution_clock::now();
    auto normalTime = duration_cast<milliseconds>(end - start).count();
    cout << "普通方法耗时: " << normalTime << "ms" << endl;
    
    // 测试离散化方法
    start = high_resolution_clock::now();
    Code16_Stars solverDisc(maxX); // 初始值不重要
    vector<int> resultDisc = solverDisc.processStarsWithDiscretization(stars);
    
    end = high_resolution_clock::now();
    auto discTime = duration_cast<milliseconds>(end - start).count();
    cout << "离散化方法耗时: " << discTime << "ms" << endl;
    
    // 验证结果是否一致
    bool isConsistent = true;
    for (int i = 0; i < n; i++) {
        if (result[i] != resultDisc[i]) {
            isConsistent = false;
            break;
        }
    }
    cout << "结果一致性验证: " << (isConsistent ? "通过" : "失败") << endl;
}

/**
 * 主函数
 */
int main() {
    // 设置随机数种子
    srand(time(nullptr));
    
    // 运行测试
    test();
    
    // 运行性能测试
    performanceTest();
    
    // 实际输入处理
    cout << "\n=== 输入测试（输入N和N个坐标）===" << endl;
    try {
        int n;
        cout << "请输入星星数量N: ";
        cin >> n;
        
        vector<pair<int, int>> stars;
        stars.reserve(n);
        int maxX = 0;
        
        for (int i = 0; i < n; i++) {
            int x, y;
            cout << "请输入第" << (i + 1) << "个星星的坐标(x y): ";
            cin >> x >> y;
            stars.emplace_back(x, y);
            maxX = max(maxX, x);
        }
        
        // 处理输入数据
        Code16_Stars solver(maxX);
        vector<int> result = solver.processStars(stars);
        
        // 输出结果
        cout << "\n输出结果：" << endl;
        Code16_Stars::printResult(result, n);
    } catch (const exception& e) {
        cout << "输入错误: " << e.what() << endl;
    }
    
    return 0;
}

/**
 * 算法总结：
 * 
 * 1. 本题的关键洞察：
 *    - 由于输入的星星是按y坐标升序排列的，所以处理每个星星时，所有已处理的星星的y坐标都不大于当前星星的y坐标
 *    - 因此，当前星星的等级就是已处理星星中x坐标小于等于当前星星x坐标的数量
 *    - 这可以通过树状数组高效地进行前缀和查询和单点更新
 * 
 * 2. 离散化的必要性：
 *    - 当x坐标范围很大时（比如到1e9），直接使用树状数组会导致空间浪费
 *    - 离散化可以将所有不同的x坐标映射到较小的连续整数范围，节省空间
 *    - 在本题中，如果x坐标范围不大，可以不使用离散化
 * 
 * 3. 树状数组操作：
 *    - update(x, val): 在位置x增加val
 *    - query(x): 查询前缀和[1,x]
 *    - lowbit(x): 获取x二进制表示中最低位的1
 * 
 * 4. 时间复杂度分析：
 *    - 树状数组的update和query操作都是O(log M)，其中M是最大x坐标值（或离散化后的坐标范围）
 *    - 处理n个星星的总时间复杂度为O(n log M)
 *    - 离散化的时间复杂度为O(n log n)
 *    - 因此总时间复杂度为O(n log n)
 * 
 * 5. 空间复杂度：
 *    - 不使用离散化：O(M)，其中M是最大x坐标值
 *    - 使用离散化：O(n)，只需存储不同的x坐标
 * 
 * 6. C++实现注意事项：
 *    - 在C++中，使用vector动态调整大小，避免数组越界问题
 *    - 使用sort和unique进行高效的去重操作
 *    - 使用unordered_map实现O(1)的查询时间复杂度
 *    - 对于大规模数据，离散化可以显著提高内存效率
 */