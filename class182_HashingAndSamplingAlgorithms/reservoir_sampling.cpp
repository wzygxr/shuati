/**
 * 蓄水池抽样算法 (Reservoir Sampling)
 * 
 * 算法原理：
 * 蓄水池抽样是一种随机抽样算法，用于从包含n个项目的未知大小的数据流中
 * 随机选择k个样本，其中n非常大或未知。该算法能够在只遍历一次数据流的情况下，
 * 保证每个元素被选中的概率相等。
 * 
 * 算法特点：
 * 1. 适用于数据流处理
 * 2. 空间复杂度为O(k)，与数据流大小无关
 * 3. 时间复杂度为O(n)
 * 4. 保证每个元素被选中的概率相等
 * 
 * 应用场景：
 * - 大数据流的随机抽样
 * - 无法预知数据总量的抽样
 * - 在线算法
 * - 数据库查询优化
 * 
 * 算法流程：
 * 1. 将前k个元素放入蓄水池
 * 2. 对于第i个元素(i > k)：
 *    a. 以 k/i 的概率选择该元素
 *    b. 如果被选中，则随机替换蓄水池中的一个元素
 * 3. 重复步骤2直到数据流结束
 * 
 * 时间复杂度：O(n)，n为数据流大小
 * 空间复杂度：O(k)，k为样本大小
 */

#include <iostream>
#include <vector>
#include <random>
#include <algorithm>
#include <iterator>
#include <chrono>

using namespace std;

class ReservoirSampling {
private:
    // 随机数生成器
    mt19937 rng;
    uniform_int_distribution<int> intDist;

public:
    /**
     * 构造函数
     */
    ReservoirSampling() : rng(chrono::steady_clock::now().time_since_epoch().count()),
                          intDist(0, 1000000) {}
    
    /**
     * 从数组中随机选择k个元素
     * 
     * @param array 输入数组
     * @param k 样本大小
     * @return 随机选择的k个元素向量
     */
    template<typename T>
    vector<T> selectRandomElements(const vector<T>& array, int k) {
        if (array.size() <= k) {
            return array;
        }
        
        // 创建结果向量
        vector<T> result(array.begin(), array.begin() + k);
        
        // 处理剩余元素
        for (size_t i = k; i < array.size(); i++) {
            // 以 k/(i+1) 的概率选择当前元素
            int j = intDist(rng) % (i + 1);
            if (j < k) {
                result[j] = array[i];
            }
        }
        
        return result;
    }
    
    /**
     * 从数据流中随机选择k个元素（使用迭代器）
     * 
     * @param begin 数据流起始迭代器
     * @param end 数据流结束迭代器
     * @param k 样本大小
     * @return 随机选择的k个元素向量
     */
    template<typename Iterator>
    vector<typename iterator_traits<Iterator>::value_type> 
    selectRandomElements(Iterator begin, Iterator end, int k) {
        using T = typename iterator_traits<Iterator>::value_type;
        vector<T> reservoir;
        
        // 将前k个元素放入蓄水池
        int count = 0;
        Iterator it = begin;
        while (it != end && count < k) {
            reservoir.push_back(*it);
            ++it;
            ++count;
        }
        
        // 如果数据流元素少于k个，直接返回
        if (count < k) {
            return reservoir;
        }
        
        // 处理剩余元素
        while (it != end) {
            count++;
            
            // 以 k/count 的概率选择当前元素
            int j = intDist(rng) % count;
            if (j < k) {
                reservoir[j] = *it;
            }
            ++it;
        }
        
        return reservoir;
    }
    
    /**
     * 从数据流中随机选择1个元素
     * 
     * @param begin 数据流起始迭代器
     * @param end 数据流结束迭代器
     * @return 随机选择的元素
     */
    template<typename Iterator>
    typename iterator_traits<Iterator>::value_type
    selectRandomElement(Iterator begin, Iterator end) {
        using T = typename iterator_traits<Iterator>::value_type;
        
        if (begin == end) {
            throw runtime_error("数据流为空");
        }
        
        T result = *begin; // 第一个元素
        int count = 1;
        
        // 遍历剩余元素
        Iterator it = begin;
        ++it;
        while (it != end) {
            count++;
            
            // 以 1/count 的概率选择当前元素
            if (intDist(rng) % count == 0) {
                result = *it;
            }
            ++it;
        }
        
        return result;
    }
};

/**
 * 测试示例
 */
int main() {
    ReservoirSampling rs;
    
    cout << "=== 蓄水池抽样算法测试 ===" << endl;
    
    // 测试从数组中随机选择元素
    cout << "\n1. 从数组中随机选择元素:" << endl;
    vector<int> array = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    cout << "原数组: ";
    for (int x : array) cout << x << " ";
    cout << endl;
    
    for (int k = 1; k <= 5; k++) {
        vector<int> selected = rs.selectRandomElements(array, k);
        cout << "选择" << k << "个元素: ";
        for (int x : selected) cout << x << " ";
        cout << endl;
    }
    
    // 测试从数据流中随机选择元素
    cout << "\n2. 从数据流中随机选择元素:" << endl;
    vector<int> streamData = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
    
    // 多次运行以验证随机性
    cout << "多次运行结果（选择3个元素）:" << endl;
    for (int i = 0; i < 5; i++) {
        vector<int> selected = rs.selectRandomElements(streamData.begin(), streamData.end(), 3);
        cout << "第" << (i + 1) << "次: ";
        for (int x : selected) cout << x << " ";
        cout << endl;
    }
    
    // 验证概率均匀性
    cout << "\n3. 验证概率均匀性（选择1个元素，运行10000次）:" << endl;
    vector<int> frequency(16, 0); // 索引0不使用
    for (int i = 0; i < 10000; i++) {
        int selected = rs.selectRandomElement(streamData.begin(), streamData.end());
        frequency[selected]++;
    }
    
    cout << "各元素被选中的频次:" << endl;
    for (int i = 1; i <= 15; i++) {
        printf("元素%d: %d次 (%.2f%%)\n", i, frequency[i], frequency[i] / 100.0);
    }
    
    return 0;
}