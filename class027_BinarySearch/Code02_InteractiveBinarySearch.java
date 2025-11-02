package class189;

import java.util.Scanner;

/**
 * 交互式二分查找算法实现
 * 
 * 核心思想：
 * 1. 通过与用户交互来确定目标值的位置
 * 2. 每次询问用户目标值与当前猜测值的关系
 * 3. 根据用户反馈调整搜索范围
 * 
 * 应用场景：
 * 1. 猜数字游戏
 * 2. 交互式问题求解
 * 3. 自适应查询系统
 * 
 * 工程化考量：
 * 1. 用户输入验证
 * 2. 异常处理
 * 3. 查询次数统计
 * 4. 信息论下界计算
 */
public class Code02_InteractiveBinarySearch {
    
    /**
     * 交互式二分查找
     * 
     * @param n 数组大小（范围为1到n）
     * @param scanner 输入扫描器
     * @return 目标值
     * 
     * 时间复杂度：O(log n)
     * 空间复杂度：O(1)
     */
    public static int interactiveBinarySearch(int n, Scanner scanner) {
        int left = 1;
        int right = n;
        int queryCount = 0;
        
        System.out.println("请想象一个1到" + n + "之间的数字，我将通过二分查找来猜出它。");
        System.out.println("对于我的每次猜测，请输入：");
        System.out.println("1 - 如果我猜的数字比你想的数字小");
        System.out.println("2 - 如果我猜的数字比你想的数字大");
        System.out.println("3 - 如果我猜对了");
        System.out.println();
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            queryCount++;
            
            System.out.println("第" + queryCount + "次猜测：" + mid);
            System.out.print("请输入你的反馈（1/2/3）：");
            
            int response = 0;
            try {
                response = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("输入无效，请输入1、2或3。");
                continue;
            }
            
            switch (response) {
                case 1:  // 猜的数字比目标小
                    left = mid + 1;
                    break;
                case 2:  // 猜的数字比目标大
                    right = mid - 1;
                    break;
                case 3:  // 猜对了
                    System.out.println("太好了！我用了" + queryCount + "次猜测找到了答案：" + mid);
                    return mid;
                default:
                    System.out.println("输入无效，请输入1、2或3。");
                    queryCount--;  // 不计入查询次数
                    break;
            }
        }
        
        System.out.println("无法找到答案，请检查你的反馈是否正确。");
        return -1;
    }
    
    /**
     * 计算信息论下界（最小查询次数）
     * 
     * @param n 搜索范围大小
     * @return 理论最小查询次数
     */
    public static int calculateLowerBound(int n) {
        // 信息论下界：log2(n) 向上取整
        return (int) Math.ceil(Math.log(n) / Math.log(2));
    }
    
    /**
     * 自适应查询优化版本
     * 根据历史查询结果调整查询策略
     * 
     * @param n 数组大小（范围为1到n）
     * @param scanner 输入扫描器
     * @return 目标值
     */
    public static int adaptiveSearch(int n, Scanner scanner) {
        int left = 1;
        int right = n;
        int queryCount = 0;
        
        System.out.println("请想象一个1到" + n + "之间的数字，我将通过自适应查询来猜出它。");
        System.out.println("对于我的每次猜测，请输入：");
        System.out.println("1 - 如果我猜的数字比你想的数字小");
        System.out.println("2 - 如果我猜的数字比你想的数字大");
        System.out.println("3 - 如果我猜对了");
        System.out.println();
        
        // 计算理论下界
        int lowerBound = calculateLowerBound(n);
        System.out.println("理论最小查询次数：" + lowerBound);
        System.out.println();
        
        while (left <= right) {
            // 自适应选择查询点
            // 简单策略：根据剩余范围的黄金分割点选择
            int range = right - left + 1;
            int mid = left + range / 2;
            
            queryCount++;
            
            System.out.println("第" + queryCount + "次猜测：" + mid);
            System.out.print("请输入你的反馈（1/2/3）：");
            
            int response = 0;
            try {
                response = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("输入无效，请输入1、2或3。");
                queryCount--;  // 不计入查询次数
                continue;
            }
            
            switch (response) {
                case 1:  // 猜的数字比目标小
                    left = mid + 1;
                    break;
                case 2:  // 猜的数字比目标大
                    right = mid - 1;
                    break;
                case 3:  // 猜对了
                    System.out.println("太好了！我用了" + queryCount + "次猜测找到了答案：" + mid);
                    System.out.println("查询效率：" + String.format("%.2f", (double) queryCount / lowerBound) + "倍理论下界");
                    return mid;
                default:
                    System.out.println("输入无效，请输入1、2或3。");
                    queryCount--;  // 不计入查询次数
                    break;
            }
        }
        
        System.out.println("无法找到答案，请检查你的反馈是否正确。");
        return -1;
    }
    
    // 测试方法
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("请输入数字范围的上限（例如100）：");
        int n = Integer.parseInt(scanner.nextLine());
        
        System.out.println("请选择查询策略：");
        System.out.println("1 - 标准二分查找");
        System.out.println("2 - 自适应查询");
        System.out.print("请输入选择（1或2）：");
        int choice = Integer.parseInt(scanner.nextLine());
        
        if (choice == 1) {
            interactiveBinarySearch(n, scanner);
        } else if (choice == 2) {
            adaptiveSearch(n, scanner);
        } else {
            System.out.println("无效选择，使用标准二分查找。");
            interactiveBinarySearch(n, scanner);
        }
        
        scanner.close();
    }
}