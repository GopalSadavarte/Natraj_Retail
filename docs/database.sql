PGDMP  1                     }            natraj_retail    17.2    17.2 �    �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                           false            �           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                           false            �           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                           false            �           1262    50051    natraj_retail    DATABASE     �   CREATE DATABASE natraj_retail WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'English_India.1252';
    DROP DATABASE natraj_retail;
                     postgres    false            �            1259    51711    bills    TABLE     t  CREATE TABLE public.bills (
    id bigint NOT NULL,
    day_wise_bill_no integer NOT NULL,
    customer_id bigint,
    user_id bigint DEFAULT '1'::bigint NOT NULL,
    payment_type character varying(255) DEFAULT 'cash'::character varying NOT NULL,
    sales_return_id bigint,
    created_at timestamp(0) without time zone,
    updated_at timestamp(0) without time zone
);
    DROP TABLE public.bills;
       public         heap r       postgres    false            �            1259    51710    bills_id_seq    SEQUENCE     u   CREATE SEQUENCE public.bills_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 #   DROP SEQUENCE public.bills_id_seq;
       public               postgres    false    232            �           0    0    bills_id_seq    SEQUENCE OWNED BY     =   ALTER SEQUENCE public.bills_id_seq OWNED BY public.bills.id;
          public               postgres    false    231            �            1259    51638 	   customers    TABLE       CREATE TABLE public.customers (
    id bigint NOT NULL,
    name character varying(255),
    contact_no character varying(10),
    user_id bigint DEFAULT '1'::bigint NOT NULL,
    created_at timestamp(0) without time zone,
    updated_at timestamp(0) without time zone
);
    DROP TABLE public.customers;
       public         heap r       postgres    false            �            1259    51637    customers_id_seq    SEQUENCE     y   CREATE SEQUENCE public.customers_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 '   DROP SEQUENCE public.customers_id_seq;
       public               postgres    false    224            �           0    0    customers_id_seq    SEQUENCE OWNED BY     E   ALTER SEQUENCE public.customers_id_seq OWNED BY public.customers.id;
          public               postgres    false    223            �            1259    51621    dealers    TABLE     �   CREATE TABLE public.dealers (
    id bigint NOT NULL,
    name character varying(255) NOT NULL,
    email character varying(255),
    contact_no character varying(10),
    gst_no character varying(255),
    user_id bigint DEFAULT '1'::bigint NOT NULL
);
    DROP TABLE public.dealers;
       public         heap r       postgres    false            �            1259    51620    dealers_id_seq    SEQUENCE     w   CREATE SEQUENCE public.dealers_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 %   DROP SEQUENCE public.dealers_id_seq;
       public               postgres    false    222            �           0    0    dealers_id_seq    SEQUENCE OWNED BY     A   ALTER SEQUENCE public.dealers_id_seq OWNED BY public.dealers.id;
          public               postgres    false    221            �            1259    51864    expiries    TABLE       CREATE TABLE public.expiries (
    id bigint NOT NULL,
    day_wise_entry_no integer NOT NULL,
    dealer_id bigint NOT NULL,
    user_id bigint DEFAULT '1'::bigint NOT NULL,
    created_at timestamp(0) without time zone,
    updated_at timestamp(0) without time zone
);
    DROP TABLE public.expiries;
       public         heap r       postgres    false            �            1259    51863    expiries_id_seq    SEQUENCE     x   CREATE SEQUENCE public.expiries_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 &   DROP SEQUENCE public.expiries_id_seq;
       public               postgres    false    248            �           0    0    expiries_id_seq    SEQUENCE OWNED BY     C   ALTER SEQUENCE public.expiries_id_seq OWNED BY public.expiries.id;
          public               postgres    false    247            �            1259    51882    expiry_entries    TABLE     f  CREATE TABLE public.expiry_entries (
    id bigint NOT NULL,
    expiry_id bigint NOT NULL,
    product_id bigint NOT NULL,
    "MRP" double precision NOT NULL,
    quantity double precision DEFAULT '1'::double precision NOT NULL,
    expiry_date date NOT NULL,
    created_at timestamp(0) without time zone,
    updated_at timestamp(0) without time zone
);
 "   DROP TABLE public.expiry_entries;
       public         heap r       postgres    false            �            1259    51881    expiry_entries_id_seq    SEQUENCE     ~   CREATE SEQUENCE public.expiry_entries_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 ,   DROP SEQUENCE public.expiry_entries_id_seq;
       public               postgres    false    250            �           0    0    expiry_entries_id_seq    SEQUENCE OWNED BY     O   ALTER SEQUENCE public.expiry_entries_id_seq OWNED BY public.expiry_entries.id;
          public               postgres    false    249            �            1259    51670    groups    TABLE     �   CREATE TABLE public.groups (
    id bigint NOT NULL,
    g_name character varying(255) NOT NULL,
    user_id bigint DEFAULT '1'::bigint NOT NULL,
    created_at timestamp(0) without time zone,
    updated_at timestamp(0) without time zone
);
    DROP TABLE public.groups;
       public         heap r       postgres    false            �            1259    51669    groups_id_seq    SEQUENCE     v   CREATE SEQUENCE public.groups_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 $   DROP SEQUENCE public.groups_id_seq;
       public               postgres    false    228            �           0    0    groups_id_seq    SEQUENCE OWNED BY     ?   ALTER SEQUENCE public.groups_id_seq OWNED BY public.groups.id;
          public               postgres    false    227            �            1259    51900    inventories    TABLE     �  CREATE TABLE public.inventories (
    id bigint NOT NULL,
    product_id bigint NOT NULL,
    current_quantity double precision NOT NULL,
    sale_rate double precision NOT NULL,
    "MRP" double precision NOT NULL,
    "MFD_date" date,
    "EXP_date" date,
    user_id bigint DEFAULT '1'::bigint NOT NULL,
    created_at timestamp(0) without time zone,
    updated_at timestamp(0) without time zone
);
    DROP TABLE public.inventories;
       public         heap r       postgres    false            �            1259    51899    inventories_id_seq    SEQUENCE     {   CREATE SEQUENCE public.inventories_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 )   DROP SEQUENCE public.inventories_id_seq;
       public               postgres    false    252            �           0    0    inventories_id_seq    SEQUENCE OWNED BY     I   ALTER SEQUENCE public.inventories_id_seq OWNED BY public.inventories.id;
          public               postgres    false    251            �            1259    51603 
   migrations    TABLE     �   CREATE TABLE public.migrations (
    id integer NOT NULL,
    migration character varying(255) NOT NULL,
    batch integer NOT NULL
);
    DROP TABLE public.migrations;
       public         heap r       postgres    false            �            1259    51602    migrations_id_seq    SEQUENCE     �   CREATE SEQUENCE public.migrations_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 (   DROP SEQUENCE public.migrations_id_seq;
       public               postgres    false    218            �           0    0    migrations_id_seq    SEQUENCE OWNED BY     G   ALTER SEQUENCE public.migrations_id_seq OWNED BY public.migrations.id;
          public               postgres    false    217            �            1259    51651    products    TABLE     �  CREATE TABLE public.products (
    id bigint NOT NULL,
    p_id bigint NOT NULL,
    barcode_no character varying(255) NOT NULL,
    group_id bigint NOT NULL,
    sub_group_id bigint NOT NULL,
    sale_rate double precision NOT NULL,
    "MRP" double precision NOT NULL,
    discount double precision DEFAULT '0'::double precision NOT NULL,
    discount_on character varying(255) DEFAULT 'sale_rate'::character varying NOT NULL,
    wholesale_rate double precision DEFAULT '0'::double precision NOT NULL,
    wholesale_quantity double precision DEFAULT '0'::double precision NOT NULL,
    user_id bigint DEFAULT '1'::bigint NOT NULL,
    created_at timestamp(0) without time zone,
    updated_at timestamp(0) without time zone
);
    DROP TABLE public.products;
       public         heap r       postgres    false            �            1259    51650    products_id_seq    SEQUENCE     x   CREATE SEQUENCE public.products_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 &   DROP SEQUENCE public.products_id_seq;
       public               postgres    false    226            �           0    0    products_id_seq    SEQUENCE OWNED BY     C   ALTER SEQUENCE public.products_id_seq OWNED BY public.products.id;
          public               postgres    false    225            �            1259    51767    purchase_entries    TABLE       CREATE TABLE public.purchase_entries (
    id bigint NOT NULL,
    purchase_id bigint NOT NULL,
    product_id bigint NOT NULL,
    purchase_rate double precision,
    sale_rate double precision NOT NULL,
    "MRP" double precision NOT NULL,
    quantity double precision DEFAULT '1'::double precision NOT NULL,
    "GST" character varying(255) DEFAULT '18%'::character varying NOT NULL,
    "MFD_date" date,
    "EXP_date" date,
    created_at timestamp(0) without time zone,
    updated_at timestamp(0) without time zone
);
 $   DROP TABLE public.purchase_entries;
       public         heap r       postgres    false            �            1259    51766    purchase_entries_id_seq    SEQUENCE     �   CREATE SEQUENCE public.purchase_entries_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 .   DROP SEQUENCE public.purchase_entries_id_seq;
       public               postgres    false    238            �           0    0    purchase_entries_id_seq    SEQUENCE OWNED BY     S   ALTER SEQUENCE public.purchase_entries_id_seq OWNED BY public.purchase_entries.id;
          public               postgres    false    237            �            1259    51804    purchase_return_entries    TABLE     �  CREATE TABLE public.purchase_return_entries (
    id bigint NOT NULL,
    purchase_return_id bigint NOT NULL,
    product_id bigint NOT NULL,
    sale_rate double precision NOT NULL,
    "MRP" double precision NOT NULL,
    quantity double precision DEFAULT '1'::double precision NOT NULL,
    "EXP_date" date,
    created_at timestamp(0) without time zone,
    updated_at timestamp(0) without time zone
);
 +   DROP TABLE public.purchase_return_entries;
       public         heap r       postgres    false            �            1259    51803    purchase_return_entries_id_seq    SEQUENCE     �   CREATE SEQUENCE public.purchase_return_entries_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 5   DROP SEQUENCE public.purchase_return_entries_id_seq;
       public               postgres    false    242            �           0    0    purchase_return_entries_id_seq    SEQUENCE OWNED BY     a   ALTER SEQUENCE public.purchase_return_entries_id_seq OWNED BY public.purchase_return_entries.id;
          public               postgres    false    241            �            1259    51786    purchase_returns    TABLE       CREATE TABLE public.purchase_returns (
    id bigint NOT NULL,
    day_wise_entry_no integer NOT NULL,
    dealer_id bigint,
    user_id bigint DEFAULT '1'::bigint NOT NULL,
    created_at timestamp(0) without time zone,
    updated_at timestamp(0) without time zone
);
 $   DROP TABLE public.purchase_returns;
       public         heap r       postgres    false            �            1259    51785    purchase_returns_id_seq    SEQUENCE     �   CREATE SEQUENCE public.purchase_returns_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 .   DROP SEQUENCE public.purchase_returns_id_seq;
       public               postgres    false    240            �           0    0    purchase_returns_id_seq    SEQUENCE OWNED BY     S   ALTER SEQUENCE public.purchase_returns_id_seq OWNED BY public.purchase_returns.id;
          public               postgres    false    239            �            1259    51749 	   purchases    TABLE       CREATE TABLE public.purchases (
    id bigint NOT NULL,
    day_wise_entry_no integer NOT NULL,
    dealer_id bigint,
    user_id bigint DEFAULT '1'::bigint NOT NULL,
    created_at timestamp(0) without time zone,
    updated_at timestamp(0) without time zone
);
    DROP TABLE public.purchases;
       public         heap r       postgres    false            �            1259    51748    purchases_id_seq    SEQUENCE     y   CREATE SEQUENCE public.purchases_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 '   DROP SEQUENCE public.purchases_id_seq;
       public               postgres    false    236            �           0    0    purchases_id_seq    SEQUENCE OWNED BY     E   ALTER SEQUENCE public.purchases_id_seq OWNED BY public.purchases.id;
          public               postgres    false    235            �            1259    51730    sales    TABLE     �  CREATE TABLE public.sales (
    id bigint NOT NULL,
    product_id bigint NOT NULL,
    bill_id bigint NOT NULL,
    new_rate double precision NOT NULL,
    new_mrp double precision NOT NULL,
    quantity double precision DEFAULT '1'::double precision NOT NULL,
    discount double precision DEFAULT '0'::double precision NOT NULL,
    created_at timestamp(0) without time zone,
    updated_at timestamp(0) without time zone
);
    DROP TABLE public.sales;
       public         heap r       postgres    false            �            1259    51729    sales_id_seq    SEQUENCE     u   CREATE SEQUENCE public.sales_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 #   DROP SEQUENCE public.sales_id_seq;
       public               postgres    false    234            �           0    0    sales_id_seq    SEQUENCE OWNED BY     =   ALTER SEQUENCE public.sales_id_seq OWNED BY public.sales.id;
          public               postgres    false    233            �            1259    51840    sales_return_entries    TABLE     �  CREATE TABLE public.sales_return_entries (
    id bigint NOT NULL,
    sales_return_id bigint NOT NULL,
    product_id bigint NOT NULL,
    sale_rate double precision NOT NULL,
    "MRP" double precision NOT NULL,
    quantity double precision DEFAULT '1'::double precision NOT NULL,
    discount double precision DEFAULT '0'::double precision NOT NULL,
    expiry_date date,
    created_at timestamp(0) without time zone,
    updated_at timestamp(0) without time zone
);
 (   DROP TABLE public.sales_return_entries;
       public         heap r       postgres    false            �            1259    51839    sales_return_entries_id_seq    SEQUENCE     �   CREATE SEQUENCE public.sales_return_entries_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 2   DROP SEQUENCE public.sales_return_entries_id_seq;
       public               postgres    false    246            �           0    0    sales_return_entries_id_seq    SEQUENCE OWNED BY     [   ALTER SEQUENCE public.sales_return_entries_id_seq OWNED BY public.sales_return_entries.id;
          public               postgres    false    245            �            1259    51822    sales_returns    TABLE       CREATE TABLE public.sales_returns (
    id bigint NOT NULL,
    day_wise_entry_no integer NOT NULL,
    customer_id bigint,
    user_id bigint DEFAULT '1'::bigint NOT NULL,
    created_at timestamp(0) without time zone,
    updated_at timestamp(0) without time zone
);
 !   DROP TABLE public.sales_returns;
       public         heap r       postgres    false            �            1259    51821    sales_returns_id_seq    SEQUENCE     }   CREATE SEQUENCE public.sales_returns_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 +   DROP SEQUENCE public.sales_returns_id_seq;
       public               postgres    false    244            �           0    0    sales_returns_id_seq    SEQUENCE OWNED BY     M   ALTER SEQUENCE public.sales_returns_id_seq OWNED BY public.sales_returns.id;
          public               postgres    false    243            �            1259    51683 
   sub_groups    TABLE       CREATE TABLE public.sub_groups (
    id bigint NOT NULL,
    sub_group_name character varying(255) NOT NULL,
    group_id bigint NOT NULL,
    user_id bigint DEFAULT '1'::bigint NOT NULL,
    created_at timestamp(0) without time zone,
    updated_at timestamp(0) without time zone
);
    DROP TABLE public.sub_groups;
       public         heap r       postgres    false            �            1259    51682    sub_groups_id_seq    SEQUENCE     z   CREATE SEQUENCE public.sub_groups_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 (   DROP SEQUENCE public.sub_groups_id_seq;
       public               postgres    false    230            �           0    0    sub_groups_id_seq    SEQUENCE OWNED BY     G   ALTER SEQUENCE public.sub_groups_id_seq OWNED BY public.sub_groups.id;
          public               postgres    false    229            �            1259    51610    users    TABLE       CREATE TABLE public.users (
    id bigint NOT NULL,
    name character varying(255) NOT NULL,
    email character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    created_at timestamp(0) without time zone,
    updated_at timestamp(0) without time zone
);
    DROP TABLE public.users;
       public         heap r       postgres    false            �            1259    51609    users_id_seq    SEQUENCE     u   CREATE SEQUENCE public.users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 #   DROP SEQUENCE public.users_id_seq;
       public               postgres    false    220            �           0    0    users_id_seq    SEQUENCE OWNED BY     =   ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;
          public               postgres    false    219            �           2604    51714    bills id    DEFAULT     d   ALTER TABLE ONLY public.bills ALTER COLUMN id SET DEFAULT nextval('public.bills_id_seq'::regclass);
 7   ALTER TABLE public.bills ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    231    232    232            �           2604    51641    customers id    DEFAULT     l   ALTER TABLE ONLY public.customers ALTER COLUMN id SET DEFAULT nextval('public.customers_id_seq'::regclass);
 ;   ALTER TABLE public.customers ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    223    224    224            �           2604    51624 
   dealers id    DEFAULT     h   ALTER TABLE ONLY public.dealers ALTER COLUMN id SET DEFAULT nextval('public.dealers_id_seq'::regclass);
 9   ALTER TABLE public.dealers ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    221    222    222            �           2604    51867    expiries id    DEFAULT     j   ALTER TABLE ONLY public.expiries ALTER COLUMN id SET DEFAULT nextval('public.expiries_id_seq'::regclass);
 :   ALTER TABLE public.expiries ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    248    247    248            �           2604    51885    expiry_entries id    DEFAULT     v   ALTER TABLE ONLY public.expiry_entries ALTER COLUMN id SET DEFAULT nextval('public.expiry_entries_id_seq'::regclass);
 @   ALTER TABLE public.expiry_entries ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    249    250    250            �           2604    51673 	   groups id    DEFAULT     f   ALTER TABLE ONLY public.groups ALTER COLUMN id SET DEFAULT nextval('public.groups_id_seq'::regclass);
 8   ALTER TABLE public.groups ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    227    228    228            �           2604    51903    inventories id    DEFAULT     p   ALTER TABLE ONLY public.inventories ALTER COLUMN id SET DEFAULT nextval('public.inventories_id_seq'::regclass);
 =   ALTER TABLE public.inventories ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    251    252    252            �           2604    51606    migrations id    DEFAULT     n   ALTER TABLE ONLY public.migrations ALTER COLUMN id SET DEFAULT nextval('public.migrations_id_seq'::regclass);
 <   ALTER TABLE public.migrations ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    217    218    218            �           2604    51654    products id    DEFAULT     j   ALTER TABLE ONLY public.products ALTER COLUMN id SET DEFAULT nextval('public.products_id_seq'::regclass);
 :   ALTER TABLE public.products ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    226    225    226            �           2604    51770    purchase_entries id    DEFAULT     z   ALTER TABLE ONLY public.purchase_entries ALTER COLUMN id SET DEFAULT nextval('public.purchase_entries_id_seq'::regclass);
 B   ALTER TABLE public.purchase_entries ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    238    237    238            �           2604    51807    purchase_return_entries id    DEFAULT     �   ALTER TABLE ONLY public.purchase_return_entries ALTER COLUMN id SET DEFAULT nextval('public.purchase_return_entries_id_seq'::regclass);
 I   ALTER TABLE public.purchase_return_entries ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    242    241    242            �           2604    51789    purchase_returns id    DEFAULT     z   ALTER TABLE ONLY public.purchase_returns ALTER COLUMN id SET DEFAULT nextval('public.purchase_returns_id_seq'::regclass);
 B   ALTER TABLE public.purchase_returns ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    240    239    240            �           2604    51752    purchases id    DEFAULT     l   ALTER TABLE ONLY public.purchases ALTER COLUMN id SET DEFAULT nextval('public.purchases_id_seq'::regclass);
 ;   ALTER TABLE public.purchases ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    235    236    236            �           2604    51733    sales id    DEFAULT     d   ALTER TABLE ONLY public.sales ALTER COLUMN id SET DEFAULT nextval('public.sales_id_seq'::regclass);
 7   ALTER TABLE public.sales ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    233    234    234            �           2604    51843    sales_return_entries id    DEFAULT     �   ALTER TABLE ONLY public.sales_return_entries ALTER COLUMN id SET DEFAULT nextval('public.sales_return_entries_id_seq'::regclass);
 F   ALTER TABLE public.sales_return_entries ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    246    245    246            �           2604    51825    sales_returns id    DEFAULT     t   ALTER TABLE ONLY public.sales_returns ALTER COLUMN id SET DEFAULT nextval('public.sales_returns_id_seq'::regclass);
 ?   ALTER TABLE public.sales_returns ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    243    244    244            �           2604    51686    sub_groups id    DEFAULT     n   ALTER TABLE ONLY public.sub_groups ALTER COLUMN id SET DEFAULT nextval('public.sub_groups_id_seq'::regclass);
 <   ALTER TABLE public.sub_groups ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    230    229    230            �           2604    51613    users id    DEFAULT     d   ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);
 7   ALTER TABLE public.users ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    219    220    220            �          0    51711    bills 
   TABLE DATA           �   COPY public.bills (id, day_wise_bill_no, customer_id, user_id, payment_type, sales_return_id, created_at, updated_at) FROM stdin;
    public               postgres    false    232   ��       �          0    51638 	   customers 
   TABLE DATA           Z   COPY public.customers (id, name, contact_no, user_id, created_at, updated_at) FROM stdin;
    public               postgres    false    224   
�       �          0    51621    dealers 
   TABLE DATA           O   COPY public.dealers (id, name, email, contact_no, gst_no, user_id) FROM stdin;
    public               postgres    false    222   '�       �          0    51864    expiries 
   TABLE DATA           e   COPY public.expiries (id, day_wise_entry_no, dealer_id, user_id, created_at, updated_at) FROM stdin;
    public               postgres    false    248   D�       �          0    51882    expiry_entries 
   TABLE DATA           y   COPY public.expiry_entries (id, expiry_id, product_id, "MRP", quantity, expiry_date, created_at, updated_at) FROM stdin;
    public               postgres    false    250   a�       �          0    51670    groups 
   TABLE DATA           M   COPY public.groups (id, g_name, user_id, created_at, updated_at) FROM stdin;
    public               postgres    false    228   ~�       �          0    51900    inventories 
   TABLE DATA           �   COPY public.inventories (id, product_id, current_quantity, sale_rate, "MRP", "MFD_date", "EXP_date", user_id, created_at, updated_at) FROM stdin;
    public               postgres    false    252   ��       �          0    51603 
   migrations 
   TABLE DATA           :   COPY public.migrations (id, migration, batch) FROM stdin;
    public               postgres    false    218   ��       �          0    51651    products 
   TABLE DATA           �   COPY public.products (id, p_id, barcode_no, group_id, sub_group_id, sale_rate, "MRP", discount, discount_on, wholesale_rate, wholesale_quantity, user_id, created_at, updated_at) FROM stdin;
    public               postgres    false    226   ��       �          0    51767    purchase_entries 
   TABLE DATA           �   COPY public.purchase_entries (id, purchase_id, product_id, purchase_rate, sale_rate, "MRP", quantity, "GST", "MFD_date", "EXP_date", created_at, updated_at) FROM stdin;
    public               postgres    false    238   ��       �          0    51804    purchase_return_entries 
   TABLE DATA           �   COPY public.purchase_return_entries (id, purchase_return_id, product_id, sale_rate, "MRP", quantity, "EXP_date", created_at, updated_at) FROM stdin;
    public               postgres    false    242   
�       �          0    51786    purchase_returns 
   TABLE DATA           m   COPY public.purchase_returns (id, day_wise_entry_no, dealer_id, user_id, created_at, updated_at) FROM stdin;
    public               postgres    false    240   '�       �          0    51749 	   purchases 
   TABLE DATA           f   COPY public.purchases (id, day_wise_entry_no, dealer_id, user_id, created_at, updated_at) FROM stdin;
    public               postgres    false    236   D�       �          0    51730    sales 
   TABLE DATA           w   COPY public.sales (id, product_id, bill_id, new_rate, new_mrp, quantity, discount, created_at, updated_at) FROM stdin;
    public               postgres    false    234   a�       �          0    51840    sales_return_entries 
   TABLE DATA           �   COPY public.sales_return_entries (id, sales_return_id, product_id, sale_rate, "MRP", quantity, discount, expiry_date, created_at, updated_at) FROM stdin;
    public               postgres    false    246   ~�       �          0    51822    sales_returns 
   TABLE DATA           l   COPY public.sales_returns (id, day_wise_entry_no, customer_id, user_id, created_at, updated_at) FROM stdin;
    public               postgres    false    244   ��       �          0    51683 
   sub_groups 
   TABLE DATA           c   COPY public.sub_groups (id, sub_group_name, group_id, user_id, created_at, updated_at) FROM stdin;
    public               postgres    false    230   ��       �          0    51610    users 
   TABLE DATA           R   COPY public.users (id, name, email, password, created_at, updated_at) FROM stdin;
    public               postgres    false    220   ��       �           0    0    bills_id_seq    SEQUENCE SET     ;   SELECT pg_catalog.setval('public.bills_id_seq', 1, false);
          public               postgres    false    231            �           0    0    customers_id_seq    SEQUENCE SET     ?   SELECT pg_catalog.setval('public.customers_id_seq', 1, false);
          public               postgres    false    223            �           0    0    dealers_id_seq    SEQUENCE SET     =   SELECT pg_catalog.setval('public.dealers_id_seq', 1, false);
          public               postgres    false    221            �           0    0    expiries_id_seq    SEQUENCE SET     >   SELECT pg_catalog.setval('public.expiries_id_seq', 1, false);
          public               postgres    false    247            �           0    0    expiry_entries_id_seq    SEQUENCE SET     D   SELECT pg_catalog.setval('public.expiry_entries_id_seq', 1, false);
          public               postgres    false    249            �           0    0    groups_id_seq    SEQUENCE SET     <   SELECT pg_catalog.setval('public.groups_id_seq', 1, false);
          public               postgres    false    227            �           0    0    inventories_id_seq    SEQUENCE SET     A   SELECT pg_catalog.setval('public.inventories_id_seq', 1, false);
          public               postgres    false    251            �           0    0    migrations_id_seq    SEQUENCE SET     @   SELECT pg_catalog.setval('public.migrations_id_seq', 19, true);
          public               postgres    false    217            �           0    0    products_id_seq    SEQUENCE SET     >   SELECT pg_catalog.setval('public.products_id_seq', 1, false);
          public               postgres    false    225            �           0    0    purchase_entries_id_seq    SEQUENCE SET     F   SELECT pg_catalog.setval('public.purchase_entries_id_seq', 1, false);
          public               postgres    false    237            �           0    0    purchase_return_entries_id_seq    SEQUENCE SET     M   SELECT pg_catalog.setval('public.purchase_return_entries_id_seq', 1, false);
          public               postgres    false    241            �           0    0    purchase_returns_id_seq    SEQUENCE SET     F   SELECT pg_catalog.setval('public.purchase_returns_id_seq', 1, false);
          public               postgres    false    239            �           0    0    purchases_id_seq    SEQUENCE SET     ?   SELECT pg_catalog.setval('public.purchases_id_seq', 1, false);
          public               postgres    false    235            �           0    0    sales_id_seq    SEQUENCE SET     ;   SELECT pg_catalog.setval('public.sales_id_seq', 1, false);
          public               postgres    false    233            �           0    0    sales_return_entries_id_seq    SEQUENCE SET     J   SELECT pg_catalog.setval('public.sales_return_entries_id_seq', 1, false);
          public               postgres    false    245            �           0    0    sales_returns_id_seq    SEQUENCE SET     C   SELECT pg_catalog.setval('public.sales_returns_id_seq', 1, false);
          public               postgres    false    243            �           0    0    sub_groups_id_seq    SEQUENCE SET     @   SELECT pg_catalog.setval('public.sub_groups_id_seq', 1, false);
          public               postgres    false    229            �           0    0    users_id_seq    SEQUENCE SET     :   SELECT pg_catalog.setval('public.users_id_seq', 1, true);
          public               postgres    false    219            �           2606    51718    bills bills_pkey 
   CONSTRAINT     N   ALTER TABLE ONLY public.bills
    ADD CONSTRAINT bills_pkey PRIMARY KEY (id);
 :   ALTER TABLE ONLY public.bills DROP CONSTRAINT bills_pkey;
       public                 postgres    false    232            �           2606    51644    customers customers_pkey 
   CONSTRAINT     V   ALTER TABLE ONLY public.customers
    ADD CONSTRAINT customers_pkey PRIMARY KEY (id);
 B   ALTER TABLE ONLY public.customers DROP CONSTRAINT customers_pkey;
       public                 postgres    false    224            �           2606    51636    dealers dealers_email_unique 
   CONSTRAINT     X   ALTER TABLE ONLY public.dealers
    ADD CONSTRAINT dealers_email_unique UNIQUE (email);
 F   ALTER TABLE ONLY public.dealers DROP CONSTRAINT dealers_email_unique;
       public                 postgres    false    222            �           2606    51629    dealers dealers_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.dealers
    ADD CONSTRAINT dealers_pkey PRIMARY KEY (id);
 >   ALTER TABLE ONLY public.dealers DROP CONSTRAINT dealers_pkey;
       public                 postgres    false    222            �           2606    51870    expiries expiries_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.expiries
    ADD CONSTRAINT expiries_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.expiries DROP CONSTRAINT expiries_pkey;
       public                 postgres    false    248            �           2606    51888 "   expiry_entries expiry_entries_pkey 
   CONSTRAINT     `   ALTER TABLE ONLY public.expiry_entries
    ADD CONSTRAINT expiry_entries_pkey PRIMARY KEY (id);
 L   ALTER TABLE ONLY public.expiry_entries DROP CONSTRAINT expiry_entries_pkey;
       public                 postgres    false    250            �           2606    51676    groups groups_pkey 
   CONSTRAINT     P   ALTER TABLE ONLY public.groups
    ADD CONSTRAINT groups_pkey PRIMARY KEY (id);
 <   ALTER TABLE ONLY public.groups DROP CONSTRAINT groups_pkey;
       public                 postgres    false    228            �           2606    51906    inventories inventories_pkey 
   CONSTRAINT     Z   ALTER TABLE ONLY public.inventories
    ADD CONSTRAINT inventories_pkey PRIMARY KEY (id);
 F   ALTER TABLE ONLY public.inventories DROP CONSTRAINT inventories_pkey;
       public                 postgres    false    252            �           2606    51608    migrations migrations_pkey 
   CONSTRAINT     X   ALTER TABLE ONLY public.migrations
    ADD CONSTRAINT migrations_pkey PRIMARY KEY (id);
 D   ALTER TABLE ONLY public.migrations DROP CONSTRAINT migrations_pkey;
       public                 postgres    false    218            �           2606    51663    products products_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.products
    ADD CONSTRAINT products_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.products DROP CONSTRAINT products_pkey;
       public                 postgres    false    226            �           2606    51774 &   purchase_entries purchase_entries_pkey 
   CONSTRAINT     d   ALTER TABLE ONLY public.purchase_entries
    ADD CONSTRAINT purchase_entries_pkey PRIMARY KEY (id);
 P   ALTER TABLE ONLY public.purchase_entries DROP CONSTRAINT purchase_entries_pkey;
       public                 postgres    false    238            �           2606    51810 4   purchase_return_entries purchase_return_entries_pkey 
   CONSTRAINT     r   ALTER TABLE ONLY public.purchase_return_entries
    ADD CONSTRAINT purchase_return_entries_pkey PRIMARY KEY (id);
 ^   ALTER TABLE ONLY public.purchase_return_entries DROP CONSTRAINT purchase_return_entries_pkey;
       public                 postgres    false    242            �           2606    51792 &   purchase_returns purchase_returns_pkey 
   CONSTRAINT     d   ALTER TABLE ONLY public.purchase_returns
    ADD CONSTRAINT purchase_returns_pkey PRIMARY KEY (id);
 P   ALTER TABLE ONLY public.purchase_returns DROP CONSTRAINT purchase_returns_pkey;
       public                 postgres    false    240            �           2606    51755    purchases purchases_pkey 
   CONSTRAINT     V   ALTER TABLE ONLY public.purchases
    ADD CONSTRAINT purchases_pkey PRIMARY KEY (id);
 B   ALTER TABLE ONLY public.purchases DROP CONSTRAINT purchases_pkey;
       public                 postgres    false    236            �           2606    51737    sales sales_pkey 
   CONSTRAINT     N   ALTER TABLE ONLY public.sales
    ADD CONSTRAINT sales_pkey PRIMARY KEY (id);
 :   ALTER TABLE ONLY public.sales DROP CONSTRAINT sales_pkey;
       public                 postgres    false    234            �           2606    51847 .   sales_return_entries sales_return_entries_pkey 
   CONSTRAINT     l   ALTER TABLE ONLY public.sales_return_entries
    ADD CONSTRAINT sales_return_entries_pkey PRIMARY KEY (id);
 X   ALTER TABLE ONLY public.sales_return_entries DROP CONSTRAINT sales_return_entries_pkey;
       public                 postgres    false    246            �           2606    51828     sales_returns sales_returns_pkey 
   CONSTRAINT     ^   ALTER TABLE ONLY public.sales_returns
    ADD CONSTRAINT sales_returns_pkey PRIMARY KEY (id);
 J   ALTER TABLE ONLY public.sales_returns DROP CONSTRAINT sales_returns_pkey;
       public                 postgres    false    244            �           2606    51689    sub_groups sub_groups_pkey 
   CONSTRAINT     X   ALTER TABLE ONLY public.sub_groups
    ADD CONSTRAINT sub_groups_pkey PRIMARY KEY (id);
 D   ALTER TABLE ONLY public.sub_groups DROP CONSTRAINT sub_groups_pkey;
       public                 postgres    false    230            �           2606    51619    users users_email_unique 
   CONSTRAINT     T   ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_email_unique UNIQUE (email);
 B   ALTER TABLE ONLY public.users DROP CONSTRAINT users_email_unique;
       public                 postgres    false    220            �           2606    51617    users users_pkey 
   CONSTRAINT     N   ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);
 :   ALTER TABLE ONLY public.users DROP CONSTRAINT users_pkey;
       public                 postgres    false    220                       2606    51719    bills bills_customer_id_foreign    FK CONSTRAINT     �   ALTER TABLE ONLY public.bills
    ADD CONSTRAINT bills_customer_id_foreign FOREIGN KEY (customer_id) REFERENCES public.customers(id);
 I   ALTER TABLE ONLY public.bills DROP CONSTRAINT bills_customer_id_foreign;
       public               postgres    false    224    4833    232                       2606    51858 #   bills bills_sales_return_id_foreign    FK CONSTRAINT     �   ALTER TABLE ONLY public.bills
    ADD CONSTRAINT bills_sales_return_id_foreign FOREIGN KEY (sales_return_id) REFERENCES public.sales_returns(id);
 M   ALTER TABLE ONLY public.bills DROP CONSTRAINT bills_sales_return_id_foreign;
       public               postgres    false    244    232    4853                       2606    51724    bills bills_user_id_foreign    FK CONSTRAINT     z   ALTER TABLE ONLY public.bills
    ADD CONSTRAINT bills_user_id_foreign FOREIGN KEY (user_id) REFERENCES public.users(id);
 E   ALTER TABLE ONLY public.bills DROP CONSTRAINT bills_user_id_foreign;
       public               postgres    false    220    4827    232            �           2606    51645 #   customers customers_user_id_foreign    FK CONSTRAINT     �   ALTER TABLE ONLY public.customers
    ADD CONSTRAINT customers_user_id_foreign FOREIGN KEY (user_id) REFERENCES public.users(id);
 M   ALTER TABLE ONLY public.customers DROP CONSTRAINT customers_user_id_foreign;
       public               postgres    false    4827    220    224            �           2606    51630    dealers dealers_user_id_foreign    FK CONSTRAINT     ~   ALTER TABLE ONLY public.dealers
    ADD CONSTRAINT dealers_user_id_foreign FOREIGN KEY (user_id) REFERENCES public.users(id);
 I   ALTER TABLE ONLY public.dealers DROP CONSTRAINT dealers_user_id_foreign;
       public               postgres    false    220    4827    222                       2606    51871 #   expiries expiries_dealer_id_foreign    FK CONSTRAINT     �   ALTER TABLE ONLY public.expiries
    ADD CONSTRAINT expiries_dealer_id_foreign FOREIGN KEY (dealer_id) REFERENCES public.dealers(id);
 M   ALTER TABLE ONLY public.expiries DROP CONSTRAINT expiries_dealer_id_foreign;
       public               postgres    false    248    4831    222                       2606    51876 !   expiries expiries_user_id_foreign    FK CONSTRAINT     �   ALTER TABLE ONLY public.expiries
    ADD CONSTRAINT expiries_user_id_foreign FOREIGN KEY (user_id) REFERENCES public.users(id);
 K   ALTER TABLE ONLY public.expiries DROP CONSTRAINT expiries_user_id_foreign;
       public               postgres    false    220    248    4827                       2606    51889 /   expiry_entries expiry_entries_expiry_id_foreign    FK CONSTRAINT     �   ALTER TABLE ONLY public.expiry_entries
    ADD CONSTRAINT expiry_entries_expiry_id_foreign FOREIGN KEY (expiry_id) REFERENCES public.expiries(id) ON DELETE CASCADE;
 Y   ALTER TABLE ONLY public.expiry_entries DROP CONSTRAINT expiry_entries_expiry_id_foreign;
       public               postgres    false    4857    248    250                       2606    51894 0   expiry_entries expiry_entries_product_id_foreign    FK CONSTRAINT     �   ALTER TABLE ONLY public.expiry_entries
    ADD CONSTRAINT expiry_entries_product_id_foreign FOREIGN KEY (product_id) REFERENCES public.products(id);
 Z   ALTER TABLE ONLY public.expiry_entries DROP CONSTRAINT expiry_entries_product_id_foreign;
       public               postgres    false    4835    226    250                       2606    51677    groups groups_user_id_foreign    FK CONSTRAINT     |   ALTER TABLE ONLY public.groups
    ADD CONSTRAINT groups_user_id_foreign FOREIGN KEY (user_id) REFERENCES public.users(id);
 G   ALTER TABLE ONLY public.groups DROP CONSTRAINT groups_user_id_foreign;
       public               postgres    false    220    228    4827                       2606    51907 *   inventories inventories_product_id_foreign    FK CONSTRAINT     �   ALTER TABLE ONLY public.inventories
    ADD CONSTRAINT inventories_product_id_foreign FOREIGN KEY (product_id) REFERENCES public.products(id);
 T   ALTER TABLE ONLY public.inventories DROP CONSTRAINT inventories_product_id_foreign;
       public               postgres    false    226    252    4835                       2606    51912 '   inventories inventories_user_id_foreign    FK CONSTRAINT     �   ALTER TABLE ONLY public.inventories
    ADD CONSTRAINT inventories_user_id_foreign FOREIGN KEY (user_id) REFERENCES public.users(id);
 Q   ALTER TABLE ONLY public.inventories DROP CONSTRAINT inventories_user_id_foreign;
       public               postgres    false    220    4827    252                        2606    51700 "   products products_group_id_foreign    FK CONSTRAINT     �   ALTER TABLE ONLY public.products
    ADD CONSTRAINT products_group_id_foreign FOREIGN KEY (group_id) REFERENCES public.groups(id);
 L   ALTER TABLE ONLY public.products DROP CONSTRAINT products_group_id_foreign;
       public               postgres    false    4837    228    226                       2606    51705 &   products products_sub_group_id_foreign    FK CONSTRAINT     �   ALTER TABLE ONLY public.products
    ADD CONSTRAINT products_sub_group_id_foreign FOREIGN KEY (sub_group_id) REFERENCES public.sub_groups(id);
 P   ALTER TABLE ONLY public.products DROP CONSTRAINT products_sub_group_id_foreign;
       public               postgres    false    230    4839    226                       2606    51664 !   products products_user_id_foreign    FK CONSTRAINT     �   ALTER TABLE ONLY public.products
    ADD CONSTRAINT products_user_id_foreign FOREIGN KEY (user_id) REFERENCES public.users(id);
 K   ALTER TABLE ONLY public.products DROP CONSTRAINT products_user_id_foreign;
       public               postgres    false    4827    226    220                       2606    51780 4   purchase_entries purchase_entries_product_id_foreign    FK CONSTRAINT     �   ALTER TABLE ONLY public.purchase_entries
    ADD CONSTRAINT purchase_entries_product_id_foreign FOREIGN KEY (product_id) REFERENCES public.products(id);
 ^   ALTER TABLE ONLY public.purchase_entries DROP CONSTRAINT purchase_entries_product_id_foreign;
       public               postgres    false    226    4835    238                       2606    51775 5   purchase_entries purchase_entries_purchase_id_foreign    FK CONSTRAINT     �   ALTER TABLE ONLY public.purchase_entries
    ADD CONSTRAINT purchase_entries_purchase_id_foreign FOREIGN KEY (purchase_id) REFERENCES public.purchases(id) ON DELETE CASCADE;
 _   ALTER TABLE ONLY public.purchase_entries DROP CONSTRAINT purchase_entries_purchase_id_foreign;
       public               postgres    false    238    4845    236                       2606    51816 B   purchase_return_entries purchase_return_entries_product_id_foreign    FK CONSTRAINT     �   ALTER TABLE ONLY public.purchase_return_entries
    ADD CONSTRAINT purchase_return_entries_product_id_foreign FOREIGN KEY (product_id) REFERENCES public.products(id);
 l   ALTER TABLE ONLY public.purchase_return_entries DROP CONSTRAINT purchase_return_entries_product_id_foreign;
       public               postgres    false    242    4835    226                       2606    51811 J   purchase_return_entries purchase_return_entries_purchase_return_id_foreign    FK CONSTRAINT     �   ALTER TABLE ONLY public.purchase_return_entries
    ADD CONSTRAINT purchase_return_entries_purchase_return_id_foreign FOREIGN KEY (purchase_return_id) REFERENCES public.purchase_returns(id) ON DELETE CASCADE;
 t   ALTER TABLE ONLY public.purchase_return_entries DROP CONSTRAINT purchase_return_entries_purchase_return_id_foreign;
       public               postgres    false    4849    242    240                       2606    51793 3   purchase_returns purchase_returns_dealer_id_foreign    FK CONSTRAINT     �   ALTER TABLE ONLY public.purchase_returns
    ADD CONSTRAINT purchase_returns_dealer_id_foreign FOREIGN KEY (dealer_id) REFERENCES public.dealers(id);
 ]   ALTER TABLE ONLY public.purchase_returns DROP CONSTRAINT purchase_returns_dealer_id_foreign;
       public               postgres    false    4831    240    222                       2606    51798 1   purchase_returns purchase_returns_user_id_foreign    FK CONSTRAINT     �   ALTER TABLE ONLY public.purchase_returns
    ADD CONSTRAINT purchase_returns_user_id_foreign FOREIGN KEY (user_id) REFERENCES public.users(id);
 [   ALTER TABLE ONLY public.purchase_returns DROP CONSTRAINT purchase_returns_user_id_foreign;
       public               postgres    false    220    4827    240                       2606    51756 %   purchases purchases_dealer_id_foreign    FK CONSTRAINT     �   ALTER TABLE ONLY public.purchases
    ADD CONSTRAINT purchases_dealer_id_foreign FOREIGN KEY (dealer_id) REFERENCES public.dealers(id);
 O   ALTER TABLE ONLY public.purchases DROP CONSTRAINT purchases_dealer_id_foreign;
       public               postgres    false    236    4831    222                       2606    51761 #   purchases purchases_user_id_foreign    FK CONSTRAINT     �   ALTER TABLE ONLY public.purchases
    ADD CONSTRAINT purchases_user_id_foreign FOREIGN KEY (user_id) REFERENCES public.users(id);
 M   ALTER TABLE ONLY public.purchases DROP CONSTRAINT purchases_user_id_foreign;
       public               postgres    false    220    236    4827            	           2606    51743    sales sales_bill_id_foreign    FK CONSTRAINT     �   ALTER TABLE ONLY public.sales
    ADD CONSTRAINT sales_bill_id_foreign FOREIGN KEY (bill_id) REFERENCES public.bills(id) ON DELETE CASCADE;
 E   ALTER TABLE ONLY public.sales DROP CONSTRAINT sales_bill_id_foreign;
       public               postgres    false    232    4841    234            
           2606    51738    sales sales_product_id_foreign    FK CONSTRAINT     �   ALTER TABLE ONLY public.sales
    ADD CONSTRAINT sales_product_id_foreign FOREIGN KEY (product_id) REFERENCES public.products(id);
 H   ALTER TABLE ONLY public.sales DROP CONSTRAINT sales_product_id_foreign;
       public               postgres    false    234    4835    226                       2606    51853 <   sales_return_entries sales_return_entries_product_id_foreign    FK CONSTRAINT     �   ALTER TABLE ONLY public.sales_return_entries
    ADD CONSTRAINT sales_return_entries_product_id_foreign FOREIGN KEY (product_id) REFERENCES public.products(id);
 f   ALTER TABLE ONLY public.sales_return_entries DROP CONSTRAINT sales_return_entries_product_id_foreign;
       public               postgres    false    4835    246    226                       2606    51848 A   sales_return_entries sales_return_entries_sales_return_id_foreign    FK CONSTRAINT     �   ALTER TABLE ONLY public.sales_return_entries
    ADD CONSTRAINT sales_return_entries_sales_return_id_foreign FOREIGN KEY (sales_return_id) REFERENCES public.sales_returns(id) ON DELETE CASCADE;
 k   ALTER TABLE ONLY public.sales_return_entries DROP CONSTRAINT sales_return_entries_sales_return_id_foreign;
       public               postgres    false    4853    246    244                       2606    51829 /   sales_returns sales_returns_customer_id_foreign    FK CONSTRAINT     �   ALTER TABLE ONLY public.sales_returns
    ADD CONSTRAINT sales_returns_customer_id_foreign FOREIGN KEY (customer_id) REFERENCES public.customers(id);
 Y   ALTER TABLE ONLY public.sales_returns DROP CONSTRAINT sales_returns_customer_id_foreign;
       public               postgres    false    224    244    4833                       2606    51834 +   sales_returns sales_returns_user_id_foreign    FK CONSTRAINT     �   ALTER TABLE ONLY public.sales_returns
    ADD CONSTRAINT sales_returns_user_id_foreign FOREIGN KEY (user_id) REFERENCES public.users(id);
 U   ALTER TABLE ONLY public.sales_returns DROP CONSTRAINT sales_returns_user_id_foreign;
       public               postgres    false    4827    244    220                       2606    51690 &   sub_groups sub_groups_group_id_foreign    FK CONSTRAINT     �   ALTER TABLE ONLY public.sub_groups
    ADD CONSTRAINT sub_groups_group_id_foreign FOREIGN KEY (group_id) REFERENCES public.groups(id);
 P   ALTER TABLE ONLY public.sub_groups DROP CONSTRAINT sub_groups_group_id_foreign;
       public               postgres    false    228    230    4837                       2606    51695 %   sub_groups sub_groups_user_id_foreign    FK CONSTRAINT     �   ALTER TABLE ONLY public.sub_groups
    ADD CONSTRAINT sub_groups_user_id_foreign FOREIGN KEY (user_id) REFERENCES public.users(id);
 O   ALTER TABLE ONLY public.sub_groups DROP CONSTRAINT sub_groups_user_id_foreign;
       public               postgres    false    230    4827    220            �      x������ � �      �      x������ � �      �      x������ � �      �      x������ � �      �      x������ � �      �      x������ � �      �      x������ � �      �     x�u��n� �g����_�/�5�j���_sR��S������1��}>v���Φ�|���@��8Z��+
:0N�_�T�T�L���uŒ��2��6�1����Jɻl?���"U-�g�Pk]ie�>�܃�9`^y�������<a8�@`Djf�<֞��ه��JwB�ں5���4t�wy�_/!AC�s�O�>PVY����6���c�U�>JUA#��h#@{���W��}�˚
�R���wB+�xF��q������m��,      �      x������ � �      �      x������ � �      �      x������ � �      �      x������ � �      �      x������ � �      �      x������ � �      �      x������ � �      �      x������ � �      �      x������ � �      �   ;   x�3�LL��̃�鹉�9z�����F�&�FF����F�
�VƖV&�ĸb���� �H0     