public class TestWaterKing {
    public static void main(String[] args) {
        // 测试用例1: [3,2,3] -> 3
        int[] nums1 = {3, 2, 3};
        System.out.println("输入: [3,2,3]");
        System.out.println("输出: " + class116.Code01_WaterKing.majorityElement(nums1));
        
        // 测试用例2: [2,2,1,1,1,2,2] -> 2
        int[] nums2 = {2, 2, 1, 1, 1, 2, 2};
        System.out.println("输入: [2,2,1,1,1,2,2]");
        System.out.println("输出: " + class116.Code01_WaterKing.majorityElement(nums2));
    }
}