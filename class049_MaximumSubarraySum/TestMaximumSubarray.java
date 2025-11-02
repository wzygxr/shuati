package class071;

public class TestMaximumSubarray {
    public static void main(String[] args) {
        // 测试用例1
        int[] nums1 = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        int result1 = Code01_MaximumProductSubarray.maxSubArray(nums1);
        System.out.println("输入数组: [-2, 1, -3, 4, -1, 2, 1, -5, 4]");
        System.out.println("最大子数组和: " + result1);
        
        // 测试用例2
        int[] nums2 = {1};
        int result2 = Code01_MaximumProductSubarray.maxSubArray(nums2);
        System.out.println("输入数组: [1]");
        System.out.println("最大子数组和: " + result2);
        
        // 测试用例3
        int[] nums3 = {5, 4, -1, 7, 8};
        int result3 = Code01_MaximumProductSubarray.maxSubArray(nums3);
        System.out.println("输入数组: [5, 4, -1, 7, 8]");
        System.out.println("最大子数组和: " + result3);
    }
}