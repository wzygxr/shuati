#include <iostream>
#include <vector>
#include <queue>
#include <random>
#include <algorithm>
#include <map>
#include <ctime>
#include <cmath>
#include <stdexcept>
#include <utility>

using namespace std;

/**
 * 加权蓄水池采样算法 (Weighted Reservoir Sampling)
 * 
 * 算法原理：
 * Efraimidis和Spirakis算法是加权蓄水池采样的经典算法。
 * 对于每个元素，计算 random()^(1/weight)，然后选择值最大的k个元素。
 * 
 * 算法步骤：
 * 1. 对于数据流中的每个元素(item, weight)：
 *    a. 计算 key = random()^(1/weight)
 *    b. 如果蓄水池未满，直接加入蓄水池
 *    c. 如果蓄水池已满，找到当前蓄水池中key最小的元素
 *    d. 如果当前元素的key大于最小key，则替换该元素
 * 
 * 时间复杂度：O(n*log(k))，其中n是数据流长度，k是蓄水池大小
 * 空间复杂度：O(k)
 * 
 * 应用场景：
 * 1. 带权重的数据流采样
 * 2. 推荐系统中的内容推荐
 * 3. 负载均衡中的服务器选择
 * 4. A/B测试中的用户分组
 */

template<typename T>
class WeightedReservoirSampler {
private:
    int reservoirSize;  // 蓄水池大小
    // 使用最小堆维护蓄水池，存储 (key, item, weight) 元组
    std::priority_queue<std::pair<double, std::pair<T, double>>, 
                       std::vector<std::pair<double, std::pair<T, double>>>, 
                       std::greater<std::pair<double, std::pair<T, double>>>> reservoir;
    std::mt19937 rng;  // 随机数生成器
    
    // 计算随机键值：random()^(1/weight)
    double calculateKey(double weight) {
        std::uniform_real_distribution<double> dist(0.0, 1.0);
        return std::pow(dist(rng), 1.0 / weight);
    }
    
public:
    WeightedReservoirSampler(int size) : reservoirSize(size), rng(std::time(nullptr)) {}
    
    /**
     * 向蓄水池中添加元素
     * @param item 元素
     * @param weight 权重
     */
    void add(const T& item, double weight) {
        if (weight <= 0) {
            throw std::invalid_argument("权重必须大于0");
        }
        
        double key = calculateKey(weight);
        
        // 如果蓄水池未满，直接加入
        if (reservoir.size() < reservoirSize) {
            reservoir.push(std::make_pair(key, std::make_pair(item, weight)));
        } else {
            // 如果蓄水池已满，比较当前元素与堆顶元素的key值
            auto smallest = reservoir.top();
            if (key > smallest.first) {
                // 替换key值最小的元素
                reservoir.pop();
                reservoir.push(std::make_pair(key, std::make_pair(item, weight)));
            }
        }
    }
    
    /**
     * 获取蓄水池中的所有元素
     * @return 元素向量
     */
    std::vector<T> getSample() {
        std::vector<T> result;
        auto temp = reservoir;  // 复制堆以避免修改原堆
        
        while (!temp.empty()) {
            result.push_back(temp.top().second.first);
            temp.pop();
        }
        
        return result;
    }
    
    /**
     * 获取蓄水池中的所有元素及权重
     * @return 元素及权重的向量
     */
    std::vector<std::pair<T, double>> getSampleWithWeights() {
        std::vector<std::pair<T, double>> result;
        auto temp = reservoir;  // 复制堆以避免修改原堆
        
        while (!temp.empty()) {
            result.push_back(temp.top().second);
            temp.pop();
        }
        
        return result;
    }
};

/**
 * 简化版本的加权采样函数
 * 适用于已知完整数据集的情况
 * @param items 元素向量
 * @param weights 权重向量
 * @param k 采样数量
 * @return 采样结果
 */
template<typename T>
std::vector<T> weightedSample(const std::vector<T>& items, 
                             const std::vector<double>& weights, 
                             int k) {
    if (items.size() != weights.size()) {
        throw std::invalid_argument("元素数量与权重数量不匹配");
    }
    
    if (k > items.size()) {
        throw std::invalid_argument("采样数量不能大于元素总数");
    }
    
    for (double weight : weights) {
        if (weight <= 0) {
            throw std::invalid_argument("权重必须大于0");
        }
    }
    
    std::mt19937 rng(std::time(nullptr));
    std::uniform_real_distribution<double> dist(0.0, 1.0);
    
    // 计算每个元素的随机键值
    std::vector<double> keys;
    for (double weight : weights) {
        // 计算 key = random()^(1/weight)
        double key = std::pow(dist(rng), 1.0 / weight);
        keys.push_back(key);
    }
    
    // 创建索引向量并按key值降序排序
    std::vector<int> indices(items.size());
    for (int i = 0; i < items.size(); i++) {
        indices[i] = i;
    }
    
    std::sort(indices.begin(), indices.end(), 
              [&keys](int a, int b) { return keys[a] > keys[b]; });
    
    // 选择前k个元素
    std::vector<T> result;
    for (int i = 0; i < k; i++) {
        result.push_back(items[indices[i]]);
    }
    
    return result;
}

/**
 * 测试函数
 */
void testWeightedReservoirSampling() {
    std::cout << "=== 加权蓄水池采样测试 ===" << std::endl;
    
    // 测试1: 使用WeightedReservoirSampler类
    std::cout << "\n测试1: 流式加权采样" << std::endl;
    WeightedReservoirSampler<std::string> sampler(3);
    
    // 模拟数据流，包含元素及其权重
    std::vector<std::string> items = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
    std::vector<double> weights = {1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0};
    
    std::cout << "数据流元素及权重:" << std::endl;
    for (int i = 0; i < items.size(); i++) {
        std::cout << items[i] << ": " << weights[i] << std::endl;
        sampler.add(items[i], weights[i]);
    }
    
    std::cout << "\n采样结果:" << std::endl;
    auto sample = sampler.getSample();
    for (const auto& item : sample) {
        std::cout << item << std::endl;
    }
    
    // 测试2: 使用简化版加权采样函数
    std::cout << "\n测试2: 完整数据集加权采样" << std::endl;
    auto sample2 = weightedSample(items, weights, 3);
    std::cout << "采样结果:" << std::endl;
    for (const auto& item : sample2) {
        std::cout << item << std::endl;
    }
    
    // 测试3: 验证权重正确性
    std::cout << "\n测试3: 权重正确性验证" << std::endl;
    std::cout << "进行10000次采样，统计各元素被选中的频率:" << std::endl;
    
    std::vector<int> counts(items.size(), 0);
    int testTimes = 10000;
    
    for (int i = 0; i < testTimes; i++) {
        auto result = weightedSample(items, weights, 1);
        std::string selectedItem = result[0];
        
        // 找到选中元素的索引
        for (int j = 0; j < items.size(); j++) {
            if (items[j] == selectedItem) {
                counts[j]++;
                break;
            }
        }
    }
    
    std::cout << "元素\t权重\t理论概率\t实际频率" << std::endl;
    double totalWeight = 0;
    for (double w : weights) {
        totalWeight += w;
    }
    
    for (int i = 0; i < items.size(); i++) {
        double theoreticalProb = weights[i] / totalWeight;
        double actualFreq = static_cast<double>(counts[i]) / testTimes;
        printf("%s\t%.1f\t%.4f\t\t%.4f\n", items[i].c_str(), weights[i], theoreticalProb, actualFreq);
    }
}

int main() {
    testWeightedReservoirSampling();
    return 0;
}