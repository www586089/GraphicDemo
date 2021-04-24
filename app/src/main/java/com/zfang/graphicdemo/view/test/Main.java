package com.zfang.graphicdemo.view.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        int result = -1;
        Solution solution = new Solution();
//        result = solution.coinChange(new int[] {186, 419, 83, 408}, 6249);
//        result = solution.coinChange(new int[] {1, 2, 5}, 11);
        result = countCharacters(new String[]{"cat","bt","hat","tree"}, "atach");
        solution.sortArray(new int[] {-4,0,7,4,9,-5,-1,0,-7,-1});
        System.out.println("result = " + result);
    }
    private int minCount = Integer.MAX_VALUE;
    public int coinChange(int[] coins, int amount) {
        if (0 == amount) {
            return 0;
        }
        Arrays.sort(coins);
        getResult(coins, amount, coins.length - 1, 0);
        return minCount == Integer.MAX_VALUE ? -1 : minCount;
    }

    public static int countCharacters(String[] words, String chars) {
        int[] charCount = new int[26];
        for (int i = 0; i < chars.length(); i++) {
            int index = chars.charAt(i) - 'a';
            if (index >= 26) {
                continue;
            }
            charCount[index] += 1;
        }
        int length = 0;
        boolean yes = true;
        int[] tmpArray = new int[26];
        OutLoop:
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            System.arraycopy(charCount, 0, tmpArray, 0, 26);
            for (int j = 0; j < word.length(); j++) {
                int index = word.charAt(j) - 'a';
                if (index >= 26) {
                    continue;
                }
                if (tmpArray[index] > 0) {
                    tmpArray[index] -= 1;
                    continue;
                }
                yes = false;
                break OutLoop;
            }
            if (yes) {
                length += word.length();
            }
        }

        return length;
    }

    private void getResult(int[] coins, int target, int start, int count) {

        for (int i = start; i >= 0; i--) {
            if (target < coins[i]) {
                continue;
            }
            int tmp = 1;
            if (target / coins[i] > 1) {
                tmp = target / coins[i];
            }
            for (int j = tmp; j >= 0; j--) {
                count += j;
                target -= coins[i] * j;
                if (count + 1 > minCount) {
                    break;
                }
                if (0 < target) {
                    getResult(coins, target, i - 1, count);
                } else if (0 == target) {
                    if (count < minCount) {
                        minCount = count;
                    }
                }
                target += coins[i] * j;
                count -= j;
            }
        }
    }


    static class Solution {
        public int[] sortArray(int[] nums) {
            qSort(nums, 0, nums.length - 1);
            return nums;
        }

        private void qSort(int[] nums, int left, int right) {
            if (left < right) {
                int partion = partion(nums, left, right, nums[left]);
                qSort(nums, left, partion - 1);
                qSort(nums, partion + 1, right);
            }
        }

        private int partion(int[] nums, int left, int right, int num) {
            int pivot = left++;
            while (left < right) {
                while (left < right && num > nums[left]) left++;
                while (right > left && num < nums[right]) right--;
                if (left == right) continue;
                swap(nums, left, right);
            }
            if (pivot != right - 1 && nums[pivot] > nums[right - 1]) {
                swap(nums, pivot, right);
            }

            return right - 1;
        }

        private void swap(int[] nums, int i, int j) {
            nums[i] = nums[i] + nums[j];
            nums[j] = nums[i] - nums[j];
            nums[i] = nums[i] - nums[j];
        }

        public int[][] findContinuousSequence(int target) {
            List<int[]> results = new ArrayList<>();
            for (int l = 1, r = 2; l < r; ) {
                int sum = (r - l + 1) * (l + r) / 2;
                if (sum == target) {
                    int[] result = new int[r - l + 1];
                    for (int i = l; i <= r; i++) {
                        result[i - l] = l;
                    }
                    l++;
                } else if (sum < target) {
                    r++;
                } else {
                    l++;
                }
            }
            return results.toArray(new int[results.size()][]);
        }
    }
}
