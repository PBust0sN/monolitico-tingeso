INSERT INTO client (client_id, avaliable, last_name, name, mail, phone_number, state, rut) VALUES
(1, TRUE, 'Gomez', 'Agustin', 'gomez.agustin@gmail.com', '933442211', 'activo', '21656345-2'),
(2, TRUE, 'Hernandez', 'Benajmin', 'hernandez.benja@gmail', '976541234', 'activo', '22567123-6'),
(3, TRUE, 'Peralta', 'Pablo', 'peralta.pablo@gmail.com', '978643531', 'activo', '12987876-2'),
(4, TRUE, 'Ramos', 'Diego', 'ramos.diego@gmail.com', '934231298', 'activo', '21453289-5'),
(5, FALSE, 'Villalobos', 'Manuel', 'villalobos.manuel@gmail.com', '923451234', 'activo', '17987345-K'),
(6, FALSE, 'Moreno', 'Daniel', 'moreno.daniel@gmail.com', '910293847', 'restringido', '98564312-4'),
(7, FALSE, 'Jimenez', 'Juan', 'jimenez.juan@gmail.com', '909347612', 'restringido', '25657321-8');

INSERT INTO roles (roles_id, type) VALUES
(1, 'admin'),
(2, 'employee');

INSERT INTO staff(staff_id, role_id, staff_mail, staff_name, staff_rut, password) VALUES
(1, 1, 'martinez.gonzalo@gmail.com', 'Gonzalo Martinez', '8456231-7', 'DameElCodigo23'),
(2, 2, 'cavieres.fran@gmail.com', 'Francisca Cavieres', '34234987-4', 'Linux123'),
(3, 2,'ortiz.marco@gmail.com', 'Marco Ortiz', '12765456-2', 'WindowsEnyoyer');

INSERT INTO loans(loan_id, client_id, staff_id, loan_type, date, delivery_date, return_date, amount, extra_charges) VALUES
(1, 1, 2, 'loan', '2025-08-25', '2025-09-25', '2025-09-09', 5000, 0),
(2, 2, 2, 'loan', '2025-07-23', '2025-07-23', '2025-08-02', 3000, 0),
(3, 3, 3, 'loan', '2025-07-31', '2025-07-31', '2025-08-15', 5000,0),
(4, 2, 3, 'return', '2025-08-17', '2025-08-17', '2025-08-17', 0,0);

-- Disponible, Prestada, En reparación, Dada de baja, (INITIAL STATES)
-- REPOSITION VALUES ARE IN CLP
INSERT INTO tools(tool_id, category, tool_name, initial_state, disponibility, reposition_fee, loan_fee, diary_fine_fee) VALUES
(1, 'Cierres Perimetrales', 'Alambre galbanizado', 'Disponible', 'Disponible', 4000, 3000, 500),
(2, 'Materiales obra pesada', 'Cemento Polpaico 25Kg', 'Disponible', 'Disponible', 6000, 2000, 600),
(3, 'Tabiqueria', 'Cinta sellador Adhesivo', 'Disponible','Disponible', 7000, 1000, 300),
(4, 'Fierro', 'Fierro A-63 12x12 mm 6mm', 'Disponible', 'Disponible', 15000, 5000, 800),
(5, 'Pasamanos y Barandas', 'Pasamanos redondo Aluminio negro 150 cm', 'Disponible', 'Disponible', 6000, 2000, 100);

INSERT INTO tools_loans(id, tool_id, loan_id) VALUES
(1, 1, 1),
(2, 2, 1),
(3, 1, 2),
(4, 4, 3),
(5, 5, 4);

-- record_types (registro nuevas herramientas, préstamo, devolución, baja, reparación)
INSERT INTO records(record_id, client_id, loan_id, tool_id, record_date, record_amount, record_type) VALUES
(1, 1, 1, NULL,'2025-08-25', 5000, 'loan'),
(2, 2, 2, NULL,'2025-07-23', 3000, 'loan'),
(3, 3, 3, NULL,'2025-07-31', 5000, 'loan'),
(4, 2, 4, NULL,'2025-08-17', 0, 'return');

INSERT INTO client_loans(id_client_loans, client_id, loan_id) VALUES
(1,1, 1),
(2, 2, 2),
(3, 2,4),
(4, 3,3);

