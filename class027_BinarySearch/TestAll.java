/**
 * 测试所有算法实现
 */
public class TestAll {
    public static void main(String[] args) {
        System.out.println("测试二分查找算法...");
        class189.Code01_BinarySearch.main(args);
        
        System.out.println("\n测试查找树根节点算法...");
        class189.Code03_FindRootInTree.main(args);
        
        System.out.println("\n测试查找图中桥边算法...");
        class189.Code04_FindBridgeInGraph.main(args);
        
        System.out.println("\n测试查找质数算法...");
        class189.Code05_FindPrime.main(args);
        
        System.out.println("\n测试信息论优化算法...");
        class189.Code07_InformationTheoreticOptimization.main(args);
        
        System.out.println("\n所有测试完成！");
    }
}