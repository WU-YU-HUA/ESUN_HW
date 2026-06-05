<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'


const seats = ref([])                 // 所有座位資料
const unassignedUsers = ref([])       // 沒有座位的員工清單
const selectedSeat = ref(null)        // 目前選中的座位物件 (綠色)
const selectedEmpId = ref('')         // 下拉選單選中的員工編號
const isLoading = ref(false)          // 載入狀態

// 後端 URL
const API_BASE = `${import.meta.env.VITE_BACKEND_URL}/api`

// ----------------------------------------------------------------
// 函數實作 (Methods)
// ----------------------------------------------------------------

// 從後端撈取資料
const fetchData = async () => {
  isLoading.value = true
  try {
    // 座位清單與未劃位員工清單
    const [seatsRes, usersRes] = await Promise.all([
      axios.get(`${API_BASE}/seats`),
      axios.get(`${API_BASE}/user_no_seat`)
    ])
    
    // 儲存資料
    seats.value = seatsRes.data.SeatChart || []
    unassignedUsers.value = usersRes.data || []
    
    // 重置選取狀態
    selectedSeat.value = null
    selectedEmpId.value = ''
  } catch (error) {
    console.error('GET資料失敗:', error)
  } finally {
    isLoading.value = false
  }
}

// 點擊座位
const handleSeatClick = (seat) => {
  // 如果點擊的是紅色座位，或者目前在載入中，就不給點
  if (!seat.available) {
    // 如果是點到已佔用的座位清空
    if (confirm(`該座位已被 ${seat.empId} 佔用。是否要清空此座位？`)) {
      releaseSeat(seat.empId)
    }
    return
  }
  
  // 點擊空位：切換成選中狀態 (綠色)
  selectedSeat.value = seat
}

//清空座位
const releaseSeat = async (empId) => {
  try {
    await axios.put(`${API_BASE}/user/${empId}`, {
      floorSeatSeq: null // 傳送 null 
    })
    alert('座位已成功清空！')
    await fetchData()
  } catch (error) {
    alert('清空座位失敗！')
  }
}

// 送出選位
const submitAssignment = async () => {
  // 防止F12 修改按鈕
  if (!selectedSeat.value) {
    alert('請先點擊一個空位（變綠色）！')
    return
  }
  if (!selectedEmpId.value) {
    alert('請選擇一位要入座的員工！')
    return
  }

  try {
    // PUT /api/user/{id}，Payload: { "floorSeatSeq": X }
    await axios.put(`${API_BASE}/user/${selectedEmpId.value}`, {
      floorSeatSeq: selectedSeat.value.floorSeatSeq
    })
    
    alert('劃位成功！')
    await fetchData() // 重新刷新畫面
  } catch (error) {
    console.error('劃位失敗:', error)
    const errorMsg = error.response?.data?.message || '劃位失敗，請重試！'
    alert(`錯誤：${errorMsg}`)
  }
}



// 網頁開啟時自動載入
onMounted(() => {
  fetchData()
})
</script>

<template>
  <div class="app-container">
    <div class="grid-container">
      <div 
        v-for="seat in seats" 
        :key="seat.floorSeatSeq"
        class="seat-box"
        :class="{
          'assigned': !seat.available,
          'selected': selectedSeat && selectedSeat.floorSeatSeq === seat.floorSeatSeq
        }"
        @click="handleSeatClick(seat)"
      >
        <span v-if="seat.available && selectedSeat && selectedSeat.floorSeatSeq === seat.floorSeatSeq">
          {{ seat.floorNo }}樓: 座位{{ seat.seatNo }} (請選擇)
        </span>
        <span v-else-if="seat.available">
          {{ seat.floorNo }}樓: 座位{{ seat.seatNo }}
        </span>
        <span v-else>
          {{ seat.floorNo }}樓: 座位{{ seat.seatNo }} [員編:{{ seat.empId }}]
        </span>
      </div>
    </div>

    <div class="legend-container">
      <div class="legend-item"><span class="box bg-empty"></span> 空位</div>
      <div class="legend-item"><span class="box bg-assigned"></span> 已佔用</div>
      <div class="legend-item"><span class="box bg-selected"></span> 請選擇</div>
    </div>

    <div class="control-panel">
      <div class="select-group">
        <label for="emp-select">選擇要劃位的員工：</label>
        <select id="emp-select" v-model="selectedEmpId">
          <option value="">-- 請選擇無座位的員工 --</option>
          <option v-for="user in unassignedUsers" :key="user.empId" :value="user.empId">
            {{ user.empId }} - {{ user.name }}
          </option>
        </select>
        <span v-if="unassignedUsers.length === 0" class="hint">（目前所有員工皆已有座位）</span>
      </div>

      <button class="btn-submit" @click="submitAssignment" :disabled="!selectedSeat || !selectedEmpId">
        送出
      </button>
    </div>
  </div>
</template>

<style scoped>
.app-container {
  max-width: 1000px;
  margin: 40px auto;
  padding: 0 20px;
  font-family: "Helvetica Neue", Helvetica, Arial, "PingFang TC", sans-serif;
}

/*一排四個 */
.grid-container {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 15px;
  margin-bottom: 25px;
}

/* 灰色空位 */
.seat-box {
  background-color: #f1f1f1;
  color: #333;
  padding: 18px;
  text-align: center;
  font-weight: bold;
  border-radius: 6px;
  cursor: pointer;
  user-select: none;
  transition: all 0.2s ease;
  border: 1px solid #e0e0e0;
  font-size: 14px;
}

.seat-box:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0,0,0,0.1);
}

/* 已佔用 */
.seat-box.assigned {
  background-color: #cc0000;
  color: #ffffff;
  border-color: #b30000;
}

/* 當前選中 */
.seat-box.selected {
  background-color: #77ff77;
  color: #333333;
  border-color: #55dd55;
}

/* 圖例 */
.legend-container {
  margin-bottom: 30px;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.legend-item {
  display: flex;
  align-items: center;
  font-size: 14px;
  font-weight: bold;
  color: #333;
}

.legend-item .box {
  width: 30px;
  height: 24px;
  display: inline-block;
  margin-right: 10px;
  border-radius: 4px;
  border: 1px solid #ccc;
}

.bg-empty { background-color: #f1f1f1; }
.bg-assigned { background-color: #cc0000; }
.bg-selected { background-color: #77ff77; }

/* 控制台與按鈕 */
.control-panel {
  margin-top: 20px;
  display: flex;
  flex-direction: column;
  gap: 15px;
  align-items: flex-start;
}

.select-group {
  font-size: 15px;
  font-weight: bold;
}

select {
  padding: 8px 12px;
  font-size: 14px;
  border-radius: 4px;
  border: 1px solid #ccc;
  outline: none;
  background-color: #fff;
}

.hint {
  color: #888;
  font-size: 13px;
  margin-left: 10px;
}

.btn-submit {
  background-color: #0044cc;
  color: white;
  border: none;
  padding: 8px 24px;
  font-size: 14px;
  font-weight: bold;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.btn-submit:hover:not(:disabled) {
  background-color: #0033aa;
}

.btn-submit:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}
</style>