DROP TABLE IF EXISTS public."user" ;

CREATE TABLE "public"."user" (
  "id" bigserial NOT NULL,
  "username" varchar(20) COLLATE "pg_catalog"."default" NOT NULL DEFAULT NULL,
  "password" varchar(32) COLLATE "pg_catalog"."default" NOT NULL DEFAULT NULL,
  "balance" numeric(12,2) NOT NULL DEFAULT 0,
  "point" int4 NOT NULL DEFAULT 0,
  "create_time" timestamp(6) NOT NULL DEFAULT now(),
  "modified_time" timestamp(6) NOT NULL DEFAULT now(),
  CONSTRAINT "user_pkey" PRIMARY KEY ("id")
)
;

ALTER TABLE "public"."user"
  OWNER TO "postgres";

COMMENT ON COLUMN "public"."user"."id" IS '用户ID';

COMMENT ON COLUMN "public"."user"."username" IS '用户名';

COMMENT ON COLUMN "public"."user"."password" IS '用户密码';

COMMENT ON COLUMN "public"."user"."create_time" IS '创建时间';

COMMENT ON COLUMN "public"."user"."modified_time" IS '修改时间';

COMMENT ON COLUMN "public"."user"."balance" IS '余额';

COMMENT ON COLUMN "public"."user"."point" IS '积分值';