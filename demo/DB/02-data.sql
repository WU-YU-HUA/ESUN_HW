-- 預設 四層 每層 四個座位
INSERT INTO seatingchart (floor_no, seat_no) VALUES (1, 1);
INSERT INTO seatingchart (floor_no, seat_no) VALUES (1, 2); 
INSERT INTO seatingchart (floor_no, seat_no) VALUES (1, 3);
INSERT INTO seatingchart (floor_no, seat_no) VALUES (1, 4);

INSERT INTO seatingchart (floor_no, seat_no) VALUES (2, 1);
INSERT INTO seatingchart (floor_no, seat_no) VALUES (2, 2); 
INSERT INTO seatingchart (floor_no, seat_no) VALUES (2, 3);
INSERT INTO seatingchart (floor_no, seat_no) VALUES (2, 4);

INSERT INTO seatingchart (floor_no, seat_no) VALUES (3, 1);
INSERT INTO seatingchart (floor_no, seat_no) VALUES (3, 2); 
INSERT INTO seatingchart (floor_no, seat_no) VALUES (3, 3);
INSERT INTO seatingchart (floor_no, seat_no) VALUES (3, 4);

INSERT INTO seatingchart (floor_no, seat_no) VALUES (4, 1);
INSERT INTO seatingchart (floor_no, seat_no) VALUES (4, 2); 
INSERT INTO seatingchart (floor_no, seat_no) VALUES (4, 3);
INSERT INTO seatingchart (floor_no, seat_no) VALUES (4, 4);

-- 預設 5位員工
INSERT INTO employee (emp_id, name, email, floor_seat_seq) 
VALUES ('10001', 'Adam', 'adam@mail.com', NULL)
ON CONFLICT (emp_id) DO NOTHING;

INSERT INTO employee (emp_id, name, email, floor_seat_seq) 
VALUES ('10002', 'Eddie', 'eddie@mail.com', NULL)
ON CONFLICT (emp_id) DO NOTHING;

INSERT INTO employee (emp_id, name, email, floor_seat_seq) 
VALUES ('10003', 'Harry', 'harry@mail.com', NULL)
ON CONFLICT (emp_id) DO NOTHING;

INSERT INTO employee (emp_id, name, email, floor_seat_seq) 
VALUES ('10004', 'Robin', 'robin@mail.com', NULL)
ON CONFLICT (emp_id) DO NOTHING;

INSERT INTO employee (emp_id, name, email, floor_seat_seq) 
VALUES ('10005', 'Jenny', 'jenny@mail.com', NULL)
ON CONFLICT (emp_id) DO NOTHING;