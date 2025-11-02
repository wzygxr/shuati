#include <iostream>
#include <vector>
#include <algorithm>
#include <chrono>

/**
 * LeetCode 853. 车队 (Car Fleet)
 * 
 * 题目描述:
 * N 辆车沿着一条车道驶向位于 target 英里之外的共同目的地。
 * 每辆车 i 以恒定的速度 speed[i] 英里/小时，从初始位置 position[i] 英里处出发。
 * 一辆车永远不会超过前面的另一辆车，但它可以追上去，并与前车以相同的速度紧接着行驶。
 * 此时，我们会忽略这两辆车之间的距离，也就是说，它们被假定处于同一位置。
 * 车队是一些由一辆或多辆车组成的非空集合，这些车以相同的速度行驶，并且彼此之间没有间隔。
 * 注意，一辆车也可以是一个车队。
 * 即便一辆车在到达目的地后不会再移动，它仍然可能是车队的一部分。
 * 
 * 返回最终车队的数量。
 * 
 * 示例1:
 * 输入: target = 12, position = [10,8,0,5,3], speed = [2,4,1,1,3]
 * 输出: 3
 * 解释:
 * 从初始位置开始，车辆按以下方式移动：
 * - 10号位置的车以2的速度移动，到达时间为(12-10)/2=1小时
 * - 8号位置的车以4的速度移动，到达时间为(12-8)/4=1小时
 * - 0号位置的车以1的速度移动，到达时间为12/1=12小时
 * - 5号位置的车以1的速度移动，到达时间为(12-5)/1=7小时
 * - 3号位置的车以3的速度移动，到达时间为(12-3)/3=3小时
 * 
 * 0号车会在12小时到达，而前面的车已经到达。
 * 3号车和5号车在到达之前都不会被前面的车阻挡。
 * 10号车和8号车会在1小时同时到达，并且形成一个车队。
 * 因此，最终车队的数量是3。
 * 
 * 示例2:
 * 输入: target = 10, position = [3], speed = [3]
 * 输出: 1
 * 
 * 示例3:
 * 输入: target = 100, position = [0,2,4], speed = [4,2,1]
 * 输出: 1
 * 解释: 0号车会在(100-0)/4=25小时到达，2号车会在(100-2)/2=49小时到达，4号车会在(100-4)/1=96小时到达。
 * 但0号车会被2号车和4号车阻挡，最终这三辆车会形成一个车队。
 * 
 * 提示:
 * 1. 0 <= N <= 10^4
 * 2. 0 < target <= 10^6
 * 3. 0 <= position[i] < target
 * 4. 0 < speed[i] <= 10^6
 * 5. 所有车的初始位置各不相同。
 * 
 * 题目链接: https://leetcode.com/problems/car-fleet/
 * 
 * 解题思路:
 * 这个问题可以通过以下步骤解决：
 * 1. 首先，我们需要将每辆车的位置和速度组合成一个对象，并按照位置从大到小（离终点近到远）排序
 * 2. 然后，计算每辆车到达终点所需的时间
 * 3. 从离终点最近的车开始，如果后面的车到达终点的时间不大于前面的车，那么后面的车会与前面的车组成一个车队
 * 4. 否则，后面的车会形成一个新的车队
 * 
 * 时间复杂度: O(n log n)，其中 n 是车的数量。排序的时间复杂度为 O(n log n)。
 * 空间复杂度: O(n)，用于存储车的信息和到达时间。
 * 
 * 这是最优解，因为我们需要至少对车辆进行一次排序，排序的时间复杂度无法低于 O(n log n)。
 */

/**
 * 计算最终车队的数量
 * 
 * @param target 目标位置
 * @param position 每辆车的初始位置数组
 * @param speed 每辆车的速度数组
 * @return 最终车队的数量
 */
int carFleet(int target, std::vector<int>& position, std::vector<int>& speed) {
    // 参数校验
    if (position.size() != speed.size()) {
        throw std::invalid_argument("参数无效：position 和 speed 数组必须长度相同");
    }
    
    int n = position.size();
    if (n == 0) {
        return 0;
    }
    
    // 创建车辆列表，存储位置和速度
    std::vector<std::pair<int, int>> cars;
    for (int i = 0; i < n; i++) {
        cars.push_back({position[i], speed[i]});
    }
    
    // 按照位置从大到小排序（离终点近到远）
    std::sort(cars.begin(), cars.end(), [](const auto& a, const auto& b) {
        return a.first > b.first;
    });
    
    int fleetCount = 1; // 至少有一个车队
    double currentTime = static_cast<double>(target - cars[0].first) / cars[0].second; // 第一辆车到达终点的时间
    
    // 从第二辆车开始，检查是否会与前面的车形成车队
    for (int i = 1; i < n; i++) {
        double arrivalTime = static_cast<double>(target - cars[i].first) / cars[i].second;
        
        // 如果当前车的到达时间大于前面车队的到达时间，那么它会形成一个新的车队
        if (arrivalTime > currentTime) {
            fleetCount++;
            currentTime = arrivalTime;
        }
        // 否则，当前车会与前面的车形成一个车队
    }
    
    return fleetCount;
}

/**
 * 另一种实现方式，使用结构体来存储车辆信息
 * 
 * @param target 目标位置
 * @param position 每辆车的初始位置数组
 * @param speed 每辆车的速度数组
 * @return 最终车队的数量
 */
int carFleetAlternative(int target, std::vector<int>& position, std::vector<int>& speed) {
    // 参数校验
    if (position.size() != speed.size()) {
        throw std::invalid_argument("参数无效：position 和 speed 数组必须长度相同");
    }
    
    int n = position.size();
    if (n == 0) {
        return 0;
    }
    
    // 定义车辆结构体
    struct Car {
        int pos;
        int spd;
    };
    
    // 创建车辆数组
    std::vector<Car> cars(n);
    for (int i = 0; i < n; i++) {
        cars[i].pos = position[i];
        cars[i].spd = speed[i];
    }
    
    // 按照位置从大到小排序
    std::sort(cars.begin(), cars.end(), [](const Car& a, const Car& b) {
        return a.pos > b.pos;
    });
    
    int fleetCount = 1;
    double prevTime = static_cast<double>(target - cars[0].pos) / cars[0].spd;
    
    for (int i = 1; i < n; i++) {
        double currTime = static_cast<double>(target - cars[i].pos) / cars[i].spd;
        if (currTime > prevTime) {
            fleetCount++;
            prevTime = currTime;
        }
    }
    
    return fleetCount;
}

/**
 * 打印数组
 * 
 * @param arr 要打印的数组
 */
void printArray(const std::vector<int>& arr) {
    std::cout << "[";
    for (size_t i = 0; i < arr.size(); i++) {
        std::cout << arr[i];
        if (i < arr.size() - 1) {
            std::cout << ", ";
        }
    }
    std::cout << "]" << std::endl;
}

// 主函数，用于测试
int main() {
    try {
        // 测试用例1
        int target1 = 12;
        std::vector<int> position1 = {10, 8, 0, 5, 3};
        std::vector<int> speed1 = {2, 4, 1, 1, 3};
        
        std::cout << "测试用例1:" << std::endl;
        std::cout << "target = " << target1 << std::endl;
        std::cout << "position = ";
        printArray(position1);
        std::cout << "speed = ";
        printArray(speed1);
        std::cout << "carFleet 结果: " << carFleet(target1, position1, speed1) << std::endl; // 预期输出: 3
        std::cout << "carFleetAlternative 结果: " << carFleetAlternative(target1, position1, speed1) << std::endl; // 预期输出: 3
        std::cout << std::endl;
        
        // 测试用例2
        int target2 = 10;
        std::vector<int> position2 = {3};
        std::vector<int> speed2 = {3};
        
        std::cout << "测试用例2:" << std::endl;
        std::cout << "target = " << target2 << std::endl;
        std::cout << "position = ";
        printArray(position2);
        std::cout << "speed = ";
        printArray(speed2);
        std::cout << "carFleet 结果: " << carFleet(target2, position2, speed2) << std::endl; // 预期输出: 1
        std::cout << "carFleetAlternative 结果: " << carFleetAlternative(target2, position2, speed2) << std::endl; // 预期输出: 1
        std::cout << std::endl;
        
        // 测试用例3
        int target3 = 100;
        std::vector<int> position3 = {0, 2, 4};
        std::vector<int> speed3 = {4, 2, 1};
        
        std::cout << "测试用例3:" << std::endl;
        std::cout << "target = " << target3 << std::endl;
        std::cout << "position = ";
        printArray(position3);
        std::cout << "speed = ";
        printArray(speed3);
        std::cout << "carFleet 结果: " << carFleet(target3, position3, speed3) << std::endl; // 预期输出: 1
        std::cout << "carFleetAlternative 结果: " << carFleetAlternative(target3, position3, speed3) << std::endl; // 预期输出: 1
        std::cout << std::endl;
        
        // 测试用例4 - 边界情况：所有车都形成一个车队
        int target4 = 100;
        std::vector<int> position4 = {90, 80, 70, 60};
        std::vector<int> speed4 = {1, 2, 3, 4};
        
        std::cout << "测试用例4:" << std::endl;
        std::cout << "target = " << target4 << std::endl;
        std::cout << "position = ";
        printArray(position4);
        std::cout << "speed = ";
        printArray(speed4);
        std::cout << "carFleet 结果: " << carFleet(target4, position4, speed4) << std::endl; // 预期输出: 1
        std::cout << "carFleetAlternative 结果: " << carFleetAlternative(target4, position4, speed4) << std::endl; // 预期输出: 1
        std::cout << std::endl;
        
        // 测试用例5 - 边界情况：所有车都各自形成一个车队
        int target5 = 100;
        std::vector<int> position5 = {90, 80, 70, 60};
        std::vector<int> speed5 = {1, 1, 1, 1};
        
        std::cout << "测试用例5:" << std::endl;
        std::cout << "target = " << target5 << std::endl;
        std::cout << "position = ";
        printArray(position5);
        std::cout << "speed = ";
        printArray(speed5);
        std::cout << "carFleet 结果: " << carFleet(target5, position5, speed5) << std::endl; // 预期输出: 4
        std::cout << "carFleetAlternative 结果: " << carFleetAlternative(target5, position5, speed5) << std::endl; // 预期输出: 4
        std::cout << std::endl;
        
        // 性能测试
        std::cout << "性能测试:" << std::endl;
        int target6 = 1000000;
        int n = 10000;
        std::vector<int> position6(n);
        std::vector<int> speed6(n);
        for (int i = 0; i < n; i++) {
            position6[i] = n - i - 1; // 从近到远排列
            speed6[i] = 1 + (i % 10); // 速度在1-10之间
        }
        
        auto startTime = std::chrono::high_resolution_clock::now();
        int result1 = carFleet(target6, position6, speed6);
        auto endTime = std::chrono::high_resolution_clock::now();
        auto duration = std::chrono::duration_cast<std::chrono::milliseconds>(endTime - startTime);
        std::cout << "大数组 - carFleet 结果: " << result1 << std::endl;
        std::cout << "大数组 - carFleet 耗时: " << duration.count() << "ms" << std::endl;
        
        startTime = std::chrono::high_resolution_clock::now();
        int result2 = carFleetAlternative(target6, position6, speed6);
        endTime = std::chrono::high_resolution_clock::now();
        duration = std::chrono::duration_cast<std::chrono::milliseconds>(endTime - startTime);
        std::cout << "大数组 - carFleetAlternative 结果: " << result2 << std::endl;
        std::cout << "大数组 - carFleetAlternative 耗时: " << duration.count() << "ms" << std::endl;
        
    } catch (const std::exception& e) {
        std::cerr << "错误: " << e.what() << std::endl;
        return 1;
    }
    
    return 0;
}