PGDMP      -                }            airflyer_db    17.0    17.0 (    %           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                           false            &           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                           false            '           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                           false            (           1262    42506    airflyer_db    DATABASE     ~   CREATE DATABASE airflyer_db WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Polish_Poland.1250';
    DROP DATABASE airflyer_db;
                     postgres    false            �            1259    42508    airports    TABLE     I  CREATE TABLE public.airports (
    id bigint NOT NULL,
    city character varying(255),
    city_photo_url character varying(255),
    code character varying(255) NOT NULL,
    continent character varying(255),
    country character varying(255),
    created_at timestamp(6) without time zone,
    name character varying(255)
);
    DROP TABLE public.airports;
       public         heap r       postgres    false            �            1259    42507    airports_id_seq    SEQUENCE     �   ALTER TABLE public.airports ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.airports_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public               postgres    false    218            �            1259    42516    flights    TABLE       CREATE TABLE public.flights (
    id bigint NOT NULL,
    airline character varying(255),
    arrival_time timestamp(6) without time zone,
    available_seats_economy integer NOT NULL,
    created_at timestamp(6) without time zone,
    departure_time timestamp(6) without time zone,
    duration character varying(255),
    flight_number character varying(255) NOT NULL,
    price_economy numeric(38,2),
    total_seats_economy integer NOT NULL,
    arrival_location_id bigint NOT NULL,
    departure_location_id bigint NOT NULL
);
    DROP TABLE public.flights;
       public         heap r       postgres    false            �            1259    42515    flights_id_seq    SEQUENCE     �   ALTER TABLE public.flights ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.flights_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public               postgres    false    220            �            1259    42523    occupied_seats_economy    TABLE     o   CREATE TABLE public.occupied_seats_economy (
    flight_id bigint NOT NULL,
    seat character varying(255)
);
 *   DROP TABLE public.occupied_seats_economy;
       public         heap r       postgres    false            �            1259    42526    registered_user_authorities    TABLE     �   CREATE TABLE public.registered_user_authorities (
    registered_user_id integer NOT NULL,
    authorities character varying(255)
);
 /   DROP TABLE public.registered_user_authorities;
       public         heap r       postgres    false            �            1259    42530    reservations    TABLE     �   CREATE TABLE public.reservations (
    id bigint NOT NULL,
    reservation_date timestamp(6) without time zone NOT NULL,
    flight_id bigint NOT NULL,
    user_id integer NOT NULL
);
     DROP TABLE public.reservations;
       public         heap r       postgres    false            �            1259    42529    reservations_id_seq    SEQUENCE     �   ALTER TABLE public.reservations ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.reservations_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public               postgres    false    224            �            1259    42535    reserved_seats    TABLE     s   CREATE TABLE public.reserved_seats (
    reservation_id bigint NOT NULL,
    seat_number character varying(255)
);
 "   DROP TABLE public.reserved_seats;
       public         heap r       postgres    false            �            1259    42539    users    TABLE     �   CREATE TABLE public.users (
    id integer NOT NULL,
    email character varying(255),
    password character varying(255) NOT NULL
);
    DROP TABLE public.users;
       public         heap r       postgres    false            �            1259    42538    users_id_seq    SEQUENCE     �   ALTER TABLE public.users ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public               postgres    false    227                      0    42508    airports 
   TABLE DATA           h   COPY public.airports (id, city, city_photo_url, code, continent, country, created_at, name) FROM stdin;
    public               postgres    false    218   �2                 0    42516    flights 
   TABLE DATA           �   COPY public.flights (id, airline, arrival_time, available_seats_economy, created_at, departure_time, duration, flight_number, price_economy, total_seats_economy, arrival_location_id, departure_location_id) FROM stdin;
    public               postgres    false    220   
:                 0    42523    occupied_seats_economy 
   TABLE DATA           A   COPY public.occupied_seats_economy (flight_id, seat) FROM stdin;
    public               postgres    false    221   �;                 0    42526    registered_user_authorities 
   TABLE DATA           V   COPY public.registered_user_authorities (registered_user_id, authorities) FROM stdin;
    public               postgres    false    222   �;                 0    42530    reservations 
   TABLE DATA           P   COPY public.reservations (id, reservation_date, flight_id, user_id) FROM stdin;
    public               postgres    false    224   <                  0    42535    reserved_seats 
   TABLE DATA           E   COPY public.reserved_seats (reservation_id, seat_number) FROM stdin;
    public               postgres    false    225   s<       "          0    42539    users 
   TABLE DATA           4   COPY public.users (id, email, password) FROM stdin;
    public               postgres    false    227   �<       )           0    0    airports_id_seq    SEQUENCE SET     >   SELECT pg_catalog.setval('public.airports_id_seq', 54, true);
          public               postgres    false    217            *           0    0    flights_id_seq    SEQUENCE SET     =   SELECT pg_catalog.setval('public.flights_id_seq', 14, true);
          public               postgres    false    219            +           0    0    reservations_id_seq    SEQUENCE SET     A   SELECT pg_catalog.setval('public.reservations_id_seq', 3, true);
          public               postgres    false    223            ,           0    0    users_id_seq    SEQUENCE SET     :   SELECT pg_catalog.setval('public.users_id_seq', 6, true);
          public               postgres    false    226            s           2606    42514    airports airports_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.airports
    ADD CONSTRAINT airports_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.airports DROP CONSTRAINT airports_pkey;
       public                 postgres    false    218            w           2606    42522    flights flights_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.flights
    ADD CONSTRAINT flights_pkey PRIMARY KEY (id);
 >   ALTER TABLE ONLY public.flights DROP CONSTRAINT flights_pkey;
       public                 postgres    false    220            {           2606    42534    reservations reservations_pkey 
   CONSTRAINT     \   ALTER TABLE ONLY public.reservations
    ADD CONSTRAINT reservations_pkey PRIMARY KEY (id);
 H   ALTER TABLE ONLY public.reservations DROP CONSTRAINT reservations_pkey;
       public                 postgres    false    224            y           2606    42549 "   flights uk6bx3i9v6ikjiy0ru5ybor8t7 
   CONSTRAINT     f   ALTER TABLE ONLY public.flights
    ADD CONSTRAINT uk6bx3i9v6ikjiy0ru5ybor8t7 UNIQUE (flight_number);
 L   ALTER TABLE ONLY public.flights DROP CONSTRAINT uk6bx3i9v6ikjiy0ru5ybor8t7;
       public                 postgres    false    220            }           2606    42551 !   users uk6dotkott2kjsp8vw4d0m25fb7 
   CONSTRAINT     ]   ALTER TABLE ONLY public.users
    ADD CONSTRAINT uk6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email);
 K   ALTER TABLE ONLY public.users DROP CONSTRAINT uk6dotkott2kjsp8vw4d0m25fb7;
       public                 postgres    false    227            u           2606    42547 $   airports uk8x5wlokxte7yksdsllxtxbjf0 
   CONSTRAINT     _   ALTER TABLE ONLY public.airports
    ADD CONSTRAINT uk8x5wlokxte7yksdsllxtxbjf0 UNIQUE (code);
 N   ALTER TABLE ONLY public.airports DROP CONSTRAINT uk8x5wlokxte7yksdsllxtxbjf0;
       public                 postgres    false    218                       2606    42545    users users_pkey 
   CONSTRAINT     N   ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);
 :   ALTER TABLE ONLY public.users DROP CONSTRAINT users_pkey;
       public                 postgres    false    227            �           2606    42567 7   registered_user_authorities fk37inp7v45p2m2cj97mtf4ajw5    FK CONSTRAINT     �   ALTER TABLE ONLY public.registered_user_authorities
    ADD CONSTRAINT fk37inp7v45p2m2cj97mtf4ajw5 FOREIGN KEY (registered_user_id) REFERENCES public.users(id);
 a   ALTER TABLE ONLY public.registered_user_authorities DROP CONSTRAINT fk37inp7v45p2m2cj97mtf4ajw5;
       public               postgres    false    222    227    4735            �           2606    42562 2   occupied_seats_economy fk3etw8rxm6i6twmh6yccmdiedn    FK CONSTRAINT     �   ALTER TABLE ONLY public.occupied_seats_economy
    ADD CONSTRAINT fk3etw8rxm6i6twmh6yccmdiedn FOREIGN KEY (flight_id) REFERENCES public.flights(id);
 \   ALTER TABLE ONLY public.occupied_seats_economy DROP CONSTRAINT fk3etw8rxm6i6twmh6yccmdiedn;
       public               postgres    false    220    4727    221            �           2606    42557 #   flights fkajnifg7iuo60ga4jh3yc4ir75    FK CONSTRAINT     �   ALTER TABLE ONLY public.flights
    ADD CONSTRAINT fkajnifg7iuo60ga4jh3yc4ir75 FOREIGN KEY (departure_location_id) REFERENCES public.airports(id);
 M   ALTER TABLE ONLY public.flights DROP CONSTRAINT fkajnifg7iuo60ga4jh3yc4ir75;
       public               postgres    false    4723    218    220            �           2606    42577 (   reservations fkb5g9io5h54iwl2inkno50ppln    FK CONSTRAINT     �   ALTER TABLE ONLY public.reservations
    ADD CONSTRAINT fkb5g9io5h54iwl2inkno50ppln FOREIGN KEY (user_id) REFERENCES public.users(id);
 R   ALTER TABLE ONLY public.reservations DROP CONSTRAINT fkb5g9io5h54iwl2inkno50ppln;
       public               postgres    false    227    4735    224            �           2606    42552 #   flights fkcqwx9qgo46jhg5c4tlewkdvtm    FK CONSTRAINT     �   ALTER TABLE ONLY public.flights
    ADD CONSTRAINT fkcqwx9qgo46jhg5c4tlewkdvtm FOREIGN KEY (arrival_location_id) REFERENCES public.airports(id);
 M   ALTER TABLE ONLY public.flights DROP CONSTRAINT fkcqwx9qgo46jhg5c4tlewkdvtm;
       public               postgres    false    218    4723    220            �           2606    42582 *   reserved_seats fkcwd72pd9h4yl47qkeuegs1mvs    FK CONSTRAINT     �   ALTER TABLE ONLY public.reserved_seats
    ADD CONSTRAINT fkcwd72pd9h4yl47qkeuegs1mvs FOREIGN KEY (reservation_id) REFERENCES public.reservations(id);
 T   ALTER TABLE ONLY public.reserved_seats DROP CONSTRAINT fkcwd72pd9h4yl47qkeuegs1mvs;
       public               postgres    false    224    225    4731            �           2606    42572 (   reservations fkix9mwp337byu4ve2jqtjurjy6    FK CONSTRAINT     �   ALTER TABLE ONLY public.reservations
    ADD CONSTRAINT fkix9mwp337byu4ve2jqtjurjy6 FOREIGN KEY (flight_id) REFERENCES public.flights(id);
 R   ALTER TABLE ONLY public.reservations DROP CONSTRAINT fkix9mwp337byu4ve2jqtjurjy6;
       public               postgres    false    220    224    4727               J  x���Kr�8���)���EQr����d�E�U1XB�(Q�$�W}�YͲc�0G����I&	PfM�ΎpD�2��c"��HtXK~ q��?^\$jŒX��ǖ��.Ďmxz���+���c��^nX���L�D��~�=�q��Ͻ&�y��G����a�V�б�����k�DӶ]��'���(:gy��0�ŭ�K"����+F:����4M�fh��.9�y!CG2�Z�L(ɒ7�:��4��o���AKn��
��$[;ըhK��̀�3�*�
H��/�L$��jl���v
[Ս�tq4\�v�^d�|��)��'��;�
�7z-��<q�	�����2�wYB�`9
��T3��4���X��2,�2D�%�H	*C��s����xj	��O ��q\�v��4�]sI�≘	l��m���G1Ar�C,L9Pj��k����S��K������8j���OLb=���z����u���Kje\>yx��ѣ� �>E6�Ջ�"��:騍ʾ�����쮀�!�wU�vO�Z��f�\%��4[;c$ s�E��h�2���*�j�I_3���˰��1��9@Jx��0_�~�u�$�/�q{$p���Fh�
7��T�!|�E���ۮQ�S���\�UU�kM��N����~wf߸;;ꎠJ�T��B��_���Mʵ�-����dxc{"��H\�=���͂ìN���5�T!Gr�":Y�Nl���X�)K�!u~�P����}�o�{�K����*(�����L�l��C�3忈�:�vi��>A�";��j��K�?k�C���{���\'L�S�	�7��<�V���*9�}r�{ABܷ��#�\ � l4K��j/d�:k���_��xa�>�(m���Q�R�`;��n��y�ƶ��j�P-%�Cj�g�~���iu>|�6X�^�U�/���" �3�^jT�!��j��7�v/lj���ݣF�ۄo�Jp	�Q�ٙ,J����D��"=c�{of�g�6��2�z�Bo�vl�:ޗd���#�Y?�=��"O]0�y 4E��xv�Ϯ�_�H���P�F6��ZGT�f���C��\�	R}Ե�`Ku��ae�/�1�U̝f�'��Kn2���9X,H�p��qP����Z���m�1�cPD{&N���7g��V�gE��i�J�����;L�/,�8`�fz�����嶀Š��k��=]Uw͓ �T�s��u�Yծ�d�V1�˘�0xTn�jK!�5�{�m} k�l	��;k�͂2ʟl�S�T�6|��g[��Q���-�.Z�z�O�q�YD�(͘|ʿ��&��O�ٲ�бY�c_��}uZ�n��Y��-���%AߠM�(�=���ءn�eP����%�(	�%Vp����Ż�ƽCβX���VT�a�awb��}�m�>-^�$��-�������m�q>[ؼ Mdv�������\8>��bC�o� ,��Q�Ǫ\�~��|�a��3�X�+�����LxZEqG�:XGm�P�Ec�ޑ+�ᴓ��GVJfBr�}�� ��;�AoԤm��X�Ԕ4:Z�W�@�� �f�r�^ځ,Ŵ�����O���l�-�KeZ����4�߿��*��zK��@�\�4���騿F�YT��w-,�Dm�7eA�x�!��p|�<����@�&����rݼ{d*�N���U�]�Kt�6��"�X��V���xjdf�jlJ��W8`�&%�2.��cť�;8��Es��-eh�=P8dOʕ��X��)˸�{;�������
=s	)ҕ�S�vr�c��|Q^�/��9��Ű����)��@MχF�����=������g��pvv�?C�         �  x��Իn�0�Y�
�@]��do�k�Թk�h3���J�޵�^b�:�%i�����z�/�4����F����@:�瓶#�M��2�i��#4~�z����˫�Pd�9���@�&a���Ѻ31/�MT�".*yx�>�#h��G��
w/+�g�����̮�ʖ����
5�PT���6�j2��th^�}�j������J�P��|��._�j�*tR\F	S@]F���NG�R_)�x��|��*_���[,�V��rd��`���kW�wT:�UCS�p^oivu��5` ����Y���~�Ĥ������Q�yEiM'좌~�(N��Aͬ��
�f��}�H{c<+�����x�����d�g�78Tkrc}��k;��+��`���?�N�n�glY�}?��#:D            x�34�4�24�4\f��F\1z\\\ 1z�         -   x�3���q�v�2Fb� �M��fHlC����ӏ+F��� 	Z         H   x�]ʹ�0��pbx�!�����q�����si,��t,&!���ɂ��+�v���q����~��?���             x�3�4�2�4b.cNC#�=... #��      "   b  x�m�Mr�0 @�5��uJ��D�)��&@4�������δ��=]!%���'U
���A�r1���!���3�D�$π[�ρ�<�g8��澫�U��>�j(�*��_0�s0��)=ˌdF�_$�qr�]��d�4o��|�^%H�.4�'��<�q?Cۨ!�m��&�Z��u������J�FN���.�US�F�Ւ��N.�v�_+�4>�1���<�t�=�zQ"�7K�	��!*T
r����]p�d�k�2J��g�(e��@�r�+�p��kt��m��JD7���ז���K�t���ud+_Pv.�:�lG+Q�!��W��jŨ~UU ]�'     