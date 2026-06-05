CREATE TABLE IF NOT EXISTS seatingchart (
    floor_seat_seq SERIAL PRIMARY KEY,
    floor_no INT NOT NULL,
    seat_no INT NOT NULL
);

CREATE TABLE IF NOT EXISTS employee (
    emp_id VARCHAR(5) PRIMARY KEY, 
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    floor_seat_seq INT UNIQUE,
    
    -- 設定外鍵關聯
    CONSTRAINT fk_seatingchart
        FOREIGN KEY (floor_seat_seq) 
        REFERENCES seatingchart(floor_seat_seq) 
        ON DELETE SET NULL
);

-- Stored Procedure
CREATE OR REPLACE PROCEDURE update_employee_seat(
    p_emp_id VARCHAR,
    p_floor_seat_seq INTEGER
)
LANGUAGE plpgsql
AS $$
BEGIN
    -- 前端傳來的座位有人坐了，就直接擋掉
    IF p_floor_seat_seq IS NOT NULL THEN
        IF EXISTS (
            SELECT 1 FROM employee 
            WHERE floor_seat_seq = p_floor_seat_seq AND emp_id <> p_emp_id
        ) THEN
            RAISE EXCEPTION '該座位已被佔用！';
        END IF;
    END IF;

    -- 執行更新：
    UPDATE employee
    SET floor_seat_seq = p_floor_seat_seq
    WHERE emp_id = p_emp_id;

END;
$$;