package com.betterjr.modules.remote.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import com.betterjr.common.annotation.BetterjrMapper;
import com.betterjr.mapper.common.Mapper;
import com.betterjr.modules.remote.entity.*;
 
@BetterjrMapper
public interface FarInfterfaceInfoMapper extends Mapper<FarInfterfaceInfo> {

    @Select("select * from t_far_function a where a.c_face = #{faceNo} ")
    @ResultType(FarFunctionInfo.class)
    public List<FarFunctionInfo> findFunctionByFaceNo(String anFaceNo);

    @Select("select * from T_FAR_FIELDMAP a where a.c_face = #{faceNo} and a.c_funcode=#{funCode} order by a.n_order ")
    @ResultType(FarFieldMapInfo.class)
    public List<FarFieldMapInfo> findFieldMapByFaceNo(@Param("faceNo") String anFaceNo, @Param("funCode") String anFunNo);

    @Select("select * from t_far_config a where a.c_face = #{faceNo}")
    @ResultType(FarConfigInfo.class)
    public List<FarConfigInfo> findConfigByFaceNo(String anFaceNo);

    @Select("select * from T_FACE_CONVERT a where a.c_face = #{faceNo} and a.C_IO=#{inutDirect}")
    @ResultType(FaceConvertInfo.class)
    public List<FaceConvertInfo> findFaceConvertByFaceNo(@Param("faceNo")String anFaceNo, @Param("inutDirect") String anInutDirect);

    @Select("select * from T_FACE_HEADER a where a.c_face = #{faceNo}")
    @ResultType(FaceHeaderInfo.class)
    public List<FaceHeaderInfo> findFaceHeadByFaceNo(String anFaceNo);

    @Select("select * from T_FACE_FIELDDICT a where a.c_face = #{faceNo} ")
    @ResultType(FaceFieldDictInfo.class)
    public List<FaceFieldDictInfo> findFaceDictByFaceNo(String anFaceNo);

    @Select(" select * from t_face_returncode a where a.c_mode = #{anModeNo} order by a.c_code")
    @ResultType(FaceReturnCode.class)
    public List<FaceReturnCode> findReturnCodeByFaceMode(String anModeNo);

    @Select(" select * from t_face_returncode a  order by  a.c_mode,  a.c_code")
    @ResultType(FaceReturnCode.class)
    public List<FaceReturnCode> findAllReturnCode();
}