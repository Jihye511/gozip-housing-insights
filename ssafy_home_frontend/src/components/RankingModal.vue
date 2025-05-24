<template>
    <div v-if="visible" class="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-40">
        <div class="bg-white p-6 rounded-lg w-full max-w-md h-[80vh] flex flex-col">
            <div class="flex justify-between items-center mb-4">
                <h2 class="text-lg font-semibold text-green-600">영역 내 시세 증감률 랭킹</h2>
                <button @click="$emit('close')" class="text-gray-400 hover:text-gray-600">✕</button>
            </div>
            <div class="overflow-y-auto grow">
                <ul class="space-y-2">
                    <li v-for="(item, idx) in data" :key="item.aptSeq"
                        class="flex justify-between items-center p-2 border rounded hover:bg-gray-50">
                        <div>
                            <p class="font-medium">{{ idx + 1 }}. {{ item.aptName }}</p>
                            <p class="text-xs text-gray-500">{{ item.address }}</p>
                        </div>
                        <span class="text-green-600 font-semibold">{{ item.rateChange }}%</span>
                    </li>
                </ul>
                <div v-if="!data.length" class="text-center text-gray-500 py-10">
                    해당 영역에 매물이 없습니다.
                </div>
            </div>
            <div class="mt-4 text-right">
                <button @click="$emit('close')" class="px-4 py-2 bg-gray-200 rounded">닫기</button>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: 'RankingModal',
    props: {
        visible: Boolean,
        data: { type: Array, default: () => [] },
    },
}
</script>

<style scoped>
/* 필요한 추가 스타일이 있으면 여기에 */
</style>
