public class TestAll {
    public static void main(String[] args) {
        // 测试Code01_MaximumSubarray
        System.out.println("Testing Code01_MaximumSubarray...");
        int[] nums1 = {-2,1,-3,4,-1,2,1,-5,4};
        System.out.println("Input: [-2,1,-3,4,-1,2,1,-5,4]");
        System.out.println("Output: " + Code01_MaximumSubarray.maxSubArray2(nums1));
        System.out.println("Expected: 6\n");
        
        // 测试Code02_HouseRobber
        System.out.println("Testing Code02_HouseRobber...");
        int[] nums2 = {2,7,9,3,1};
        System.out.println("Input: [2,7,9,3,1]");
        System.out.println("Output: " + Code02_HouseRobber.rob2(nums2));
        System.out.println("Expected: 12\n");
        
        // 测试Code03_MaximumSumCircularSubarray
        System.out.println("Testing Code03_MaximumSumCircularSubarray...");
        int[] nums3 = {1,-2,3,-2};
        System.out.println("Input: [1,-2,3,-2]");
        System.out.println("Output: " + Code03_MaximumSumCircularSubarray.maxSubarraySumCircular(nums3));
        System.out.println("Expected: 3\n");
        
        // 测试Code04_HouseRobberII
        System.out.println("Testing Code04_HouseRobberII...");
        int[] nums4 = {2,3,2};
        System.out.println("Input: [2,3,2]");
        System.out.println("Output: " + Code04_HouseRobberII.rob(nums4));
        System.out.println("Expected: 3\n");
        
        // 测试Code05_HouseRobberIV
        System.out.println("Testing Code05_HouseRobberIV...");
        int[] nums5 = {2,3,5,9};
        int k = 2;
        System.out.println("Input: [2,3,5,9], k=2");
        System.out.println("Output: " + Code05_HouseRobberIV.minCapability(nums5, k));
        System.out.println("Expected: 5\n");
        
        // 测试Code06_MaximumSubmatrix
        System.out.println("Testing Code06_MaximumSubmatrix...");
        int[][] grid = {{1,2,-1,-4,-20}, {-8,-3,4,2,1}, {3,8,10,1,3}, {-4,-1,1,7,-6}};
        System.out.println("Input: 4x5 matrix");
        int[] result = Code06_MaximumSubmatrix.getMaxMatrix(grid);
        System.out.println("Output: [" + result[0] + ", " + result[1] + ", " + result[2] + ", " + result[3] + "]");
        System.out.println("Expected: [1, 1, 2, 3] (submatrix with sum 29)\n");
        
        // 测试Code07_MaximumProductSubarray
        System.out.println("Testing Code07_MaximumProductSubarray...");
        int[] nums7 = {2,3,-2,4};
        System.out.println("Input: [2,3,-2,4]");
        System.out.println("Output: " + Code07_MaximumProductSubarray.maxProduct(nums7));
        System.out.println("Expected: 6\n");
        
        // 测试Code08_DeleteAndEarn
        System.out.println("Testing Code08_DeleteAndEarn...");
        int[] nums8 = {3,4,2};
        System.out.println("Input: [3,4,2]");
        System.out.println("Output: " + Code08_DeleteAndEarn.deleteAndEarnOptimized(nums8));
        System.out.println("Expected: 6\n");
        
        System.out.println("All tests completed!");
    }
}