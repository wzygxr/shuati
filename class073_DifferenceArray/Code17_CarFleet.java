package class047;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
public class Code17_CarFleet {
    
    /**
     * 车辆类，用于存储车辆的位置和速度
     */
    private static class Car {
        int position;
        int speed;
        
        public Car(int position, int speed) {
            this.position = position;
            this.speed = speed;
        }
    }
    
    /**
     * 计算最终车队的数量
     * 
     * @param target 目标位置
     * @param position 每辆车的初始位置数组
     * @param speed 每辆车的速度数组
     * @return 最终车队的数量
     */
    public static int carFleet(int target, int[] position, int[] speed) {
        // 参数校验
        if (position == null || speed == null || position.length != speed.length) {
            throw new IllegalArgumentException("参数无效：position 和 speed 数组必须长度相同且非空");
        }
        
        int n = position.length;
        if (n == 0) {
            return 0;
        }
        
        // 创建车辆列表
        List<Car> cars = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            cars.add(new Car(position[i], speed[i]));
        }
        
        // 按照位置从大到小排序（离终点近到远）
        Collections.sort(cars, (a, b) -> b.position - a.position);
        
        int fleetCount = 1; // 至少有一个车队
        double currentTime = (double)(target - cars.get(0).position) / cars.get(0).speed; // 第一辆车到达终点的时间
        
        // 从第二辆车开始，检查是否会与前面的车形成车队
        for (int i = 1; i < n; i++) {
            double arrivalTime = (double)(target - cars.get(i).position) / cars.get(i).speed;
            
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
     * 另一种实现方式，使用数组而不是列表
     * 
     * @param target 目标位置
     * @param position 每辆车的初始位置数组
     * @param speed 每辆车的速度数组
     * @return 最终车队的数量
     */
    public static int carFleetAlternative(int target, int[] position, int[] speed) {
        // 参数校验
        if (position == null || speed == null || position.length != speed.length) {
            throw new IllegalArgumentException("参数无效：position 和 speed 数组必须长度相同且非空");
        }
        
        int n = position.length;
        if (n == 0) {
            return 0;
        }
        
        // 创建一个二维数组，存储位置和速度
        int[][] cars = new int[n][2];
        for (int i = 0; i < n; i++) {
            cars[i][0] = position[i];
            cars[i][1] = speed[i];
        }
        
        // 按照位置从大到小排序
        java.util.Arrays.sort(cars, (a, b) -> b[0] - a[0]);
        
        int fleetCount = 1;
        double prevTime = (double)(target - cars[0][0]) / cars[0][1];
        
        for (int i = 1; i < n; i++) {
            double currTime = (double)(target - cars[i][0]) / cars[i][1];
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
    public static void printArray(int[] arr) {
        System.out.print("[");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]);
            if (i < arr.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }
    
    /**
     * 测试用例
     */
    public static void main(String[] args) {
        // 测试用例1
        int target1 = 12;
        int[] position1 = {10, 8, 0, 5, 3};
        int[] speed1 = {2, 4, 1, 1, 3};
        
        System.out.println("测试用例1:");
        System.out.println("target = " + target1);
        System.out.print("position = ");
        printArray(position1);
        System.out.print("speed = ");
        printArray(speed1);
        System.out.println("carFleet 结果: " + carFleet(target1, position1, speed1)); // 预期输出: 3
        System.out.println("carFleetAlternative 结果: " + carFleetAlternative(target1, position1, speed1)); // 预期输出: 3
        System.out.println();
        
        // 测试用例2
        int target2 = 10;
        int[] position2 = {3};
        int[] speed2 = {3};
        
        System.out.println("测试用例2:");
        System.out.println("target = " + target2);
        System.out.print("position = ");
        printArray(position2);
        System.out.print("speed = ");
        printArray(speed2);
        System.out.println("carFleet 结果: " + carFleet(target2, position2, speed2)); // 预期输出: 1
        System.out.println("carFleetAlternative 结果: " + carFleetAlternative(target2, position2, speed2)); // 预期输出: 1
        System.out.println();
        
        // 测试用例3
        int target3 = 100;
        int[] position3 = {0, 2, 4};
        int[] speed3 = {4, 2, 1};
        
        System.out.println("测试用例3:");
        System.out.println("target = " + target3);
        System.out.print("position = ");
        printArray(position3);
        System.out.print("speed = ");
        printArray(speed3);
        System.out.println("carFleet 结果: " + carFleet(target3, position3, speed3)); // 预期输出: 1
        System.out.println("carFleetAlternative 结果: " + carFleetAlternative(target3, position3, speed3)); // 预期输出: 1
        System.out.println();
        
        // 测试用例4 - 边界情况：所有车都形成一个车队
        int target4 = 100;
        int[] position4 = {90, 80, 70, 60};
        int[] speed4 = {1, 2, 3, 4};
        
        System.out.println("测试用例4:");
        System.out.println("target = " + target4);
        System.out.print("position = ");
        printArray(position4);
        System.out.print("speed = ");
        printArray(speed4);
        System.out.println("carFleet 结果: " + carFleet(target4, position4, speed4)); // 预期输出: 1
        System.out.println("carFleetAlternative 结果: " + carFleetAlternative(target4, position4, speed4)); // 预期输出: 1
        System.out.println();
        
        // 测试用例5 - 边界情况：所有车都各自形成一个车队
        int target5 = 100;
        int[] position5 = {90, 80, 70, 60};
        int[] speed5 = {1, 1, 1, 1};
        
        System.out.println("测试用例5:");
        System.out.println("target = " + target5);
        System.out.print("position = ");
        printArray(position5);
        System.out.print("speed = ");
        printArray(speed5);
        System.out.println("carFleet 结果: " + carFleet(target5, position5, speed5)); // 预期输出: 4
        System.out.println("carFleetAlternative 结果: " + carFleetAlternative(target5, position5, speed5)); // 预期输出: 4
        System.out.println();
        
        // 性能测试
        System.out.println("性能测试:");
        int target6 = 1000000;
        int n = 10000;
        int[] position6 = new int[n];
        int[] speed6 = new int[n];
        for (int i = 0; i < n; i++) {
            position6[i] = n - i - 1; // 从近到远排列
            speed6[i] = 1 + (i % 10); // 速度在1-10之间
        }
        
        long startTime = System.currentTimeMillis();
        int result1 = carFleet(target6, position6, speed6);
        long endTime = System.currentTimeMillis();
        System.out.println("大数组 - carFleet 结果: " + result1);
        System.out.println("大数组 - carFleet 耗时: " + (endTime - startTime) + "ms");
        
        startTime = System.currentTimeMillis();
        int result2 = carFleetAlternative(target6, position6, speed6);
        endTime = System.currentTimeMillis();
        System.out.println("大数组 - carFleetAlternative 结果: " + result2);
        System.out.println("大数组 - carFleetAlternative 耗时: " + (endTime - startTime) + "ms");
    }
}